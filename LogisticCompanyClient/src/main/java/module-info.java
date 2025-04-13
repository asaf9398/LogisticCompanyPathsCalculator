module LogisticCompanyClient {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.media;
    requires javafx.web;
    requires com.google.gson;
    requires com.sothawo.mapjfx;
    requires org.slf4j;
    requires jdk.jfr;
    requires org.mockito;
    requires org.hamcrest;
    requires junit;
    requires net.bytebuddy;
    requires net.bytebuddy.agent;



    opens models to com.google.gson;
    opens client to com.google.gson;
    opens networking to com.google.gson;
    opens ui to javafx.fxml, com.google.gson;
    opens tests to junit,org.mockito,net.bytebuddy;

    exports ui;
    exports  client to org.mockito;
}
