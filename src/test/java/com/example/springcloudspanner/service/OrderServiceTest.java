package com.example.springcloudspanner.service;

import com.example.springcloudspanner.SpringCloudSpannerApplication;
import com.example.springcloudspanner.model.Order;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.cloud.NoCredentials;
import com.google.cloud.spanner.*;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.SpannerEmulatorContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Testcontainers
@SpringBootTest(classes = SpringCloudSpannerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class OrderServiceTest {

    private static final String PROJECT_ID = "test-project";
    private static final String INSTANCE_ID = "test-instance";

    @Container
    private static final SpannerEmulatorContainer spannerEmulator =
            new SpannerEmulatorContainer(
                    DockerImageName.parse("gcr.io/cloud-spanner-emulator/emulator:1.1.1"));

    @DynamicPropertySource
    static void emulatorProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.cloud.gcp.spanner.emulator-host", spannerEmulator::getEmulatorGrpcEndpoint);
    }

    @TestConfiguration
    static class EmulatorConfiguration {
        @Bean
        NoCredentialsProvider googleCredentials() {
            return NoCredentialsProvider.create();
        }
    }


    private InstanceId createInstance(Spanner spanner) throws InterruptedException, ExecutionException {
        InstanceConfigId instanceConfig = InstanceConfigId.of(PROJECT_ID, "emulator-config");
        InstanceId instanceId = InstanceId.of(PROJECT_ID, INSTANCE_ID);
        InstanceAdminClient insAdminClient = spanner.getInstanceAdminClient();
        Instance instance = insAdminClient
                .createInstance(
                        InstanceInfo
                                .newBuilder(instanceId)
                                .setNodeCount(1)
                                .setDisplayName("Test instance")
                                .setInstanceConfigId(instanceConfig)
                                .build()
                )
                .get();
        return instanceId;
    }

    private void createDatabase(Spanner spanner) throws InterruptedException, ExecutionException {
        DatabaseAdminClient dbAdminClient = spanner.getDatabaseAdminClient();
        System.out.println("-- dbAdminClient --"+dbAdminClient);
        Database database = dbAdminClient
                .createDatabase(
                        INSTANCE_ID,
                        "order_schema",
                        Arrays.asList("CREATE TABLE Orders (orderId INT64 NOT NULL, name STRING(255), order_status STRING(255)) PRIMARY KEY (orderId)")
                )
                .get();
        System.out.println("-- database --"+database);
    }

    private Spanner spanner(){
        SpannerOptions options = SpannerOptions
                .newBuilder()
                .setEmulatorHost(spannerEmulator.getEmulatorGrpcEndpoint())
                .setCredentials(NoCredentials.getInstance())
                .setProjectId(PROJECT_ID)
                .build();
        Spanner spanner = options.getService();
        return spanner;
    }

    @Autowired
    private OrderService orderService;

    @Before
    public static void setup() throws ExecutionException, InterruptedException {
    }

    @Test
    public void test() throws ExecutionException, InterruptedException {

        Spanner spanner = spanner();
        InstanceId instanceId = createInstance(spanner);
        createDatabase(spanner);

        Order order = new Order();
        order.setOrder_status("COMPLETED");
        order.setName("Order-1");

        String message = this.orderService.save(order);
        Assertions.assertEquals("Order Saved Successfully", message);

        List<Order> orders = this.orderService.findOrdersByName("Order-1");
        Assertions.assertTrue(orders.size() == 1);
        Assertions.assertTrue(orders.get(0).getOrder_status().equals("COMPLETED"));

    }

    @AfterAll
    public static void tearDown(){
        spannerEmulator.close();
    }
}
