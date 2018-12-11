package com.dxc.minifilesharing.file.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class MyWebAppInitializer implements WebApplicationInitializer {
    private String TMP_FOLDER = "/tmp";
    private int MAX_UPLOAD_SIZE = 5 * 1024 * 1024;

    @Override
    public void onStartup(ServletContext sc) throws ServletException {

        ServletRegistration.Dynamic appServlet = sc.addServlet("mvc", new DispatcherServlet(
                new GenericWebApplicationContext()));

        appServlet.setLoadOnStartup(1);

        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(TMP_FOLDER,
                MAX_UPLOAD_SIZE, MAX_UPLOAD_SIZE * 2, MAX_UPLOAD_SIZE / 2);

        appServlet.setMultipartConfig(multipartConfigElement);
    }
//    @Override
//    public void onStartup(ServletContext container) {
        // Create the 'root' Spring application context
//        AnnotationConfigWebApplicationContext rootContext =
//                new AnnotationConfigWebApplicationContext();
//        rootContext.register(AppConfiguration.class);

        // Manage the lifecycle of the root application context
//        container.addListener(new ContextLoaderListener(rootContext));


        // Register and map the dispatcher servlet




        // Create the dispatcher servlet's Spring application context
//        final AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
//        ServletRegistration.Dynamic dispatcher =
//                container.addServlet("dispatcher", new DispatcherServlet(appContext));
//        dispatcher.setLoadOnStartup(1);
//        dispatcher.addMapping("/");
//        registration.addMapping("/");

//        File uploadDirectory = new File(System.getProperty("java.io.tmpdir"));
//        MultipartConfigElement multipartConfigElement = new  MultipartConfigElement(uploadDirectory.getAbsolutePath(), 100000, 100000 * 2, 100000 / 2);

//        registration.setMultipartConfig(multipartConfigElement);

//        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
//        rootContext.register(AppConfiguration.class);
//        ContextLoaderListener contextLoaderListener = new ContextLoaderListener(rootContext);
//        container.addListener(contextLoaderListener);
//        container.setInitParameter("contextInitializerClasses", "mvctest.web.DemoApplicationContextInitializer");
//        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
//        webContext.register(MvcConfiguration.class);
//        DispatcherServlet dispatcherServlet = new DispatcherServlet(webContext);
//        ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", dispatcherServlet);
//        dispatcher.addMapping("/");


//    }

}
