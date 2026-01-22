package com.learning.iam;

import com.learning.iam.secretvaults.SecretsVaultPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * Hello world!
 *
 */
@ComponentScan
@SpringBootApplication
@PropertySource(name="vaultProperties", value={""}, factory = SecretsVaultPropertySourceFactory.class)
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(App.class);
    }
}
