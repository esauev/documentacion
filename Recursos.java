===== JOINS =====

// Muchos a uno: Employee with department
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "department_id") 
private Department department;


// Uno a muchos: Employee with Phone
@OneToMany(mappedBy = "employee")
private List<Phone> phones;

EJEMPLO COMPLETO

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private int age;

    @ManyToOne
    private Department department;

    @OneToMany(mappedBy = "employee")
    private List<Phone> phones;

    // getters and setters...
}

@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;

    // getters and setters...
}

@Entity
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String number;

    @ManyToOne
    private Employee employee;

    // getters and setters...
}

==== MOSTRAR QUE TAMBIEN SE PUEDEN CREAR LOS PROYECTOS DESDE WEB Y DESPUES IMPORTARLOS A STS4
https://start.spring.io/

==== CONFIG'S MICROSERVICES ====
==== EUREKA SERVER
1. CREAR PROYECTO DE EUREKA
	1.1 Elegir solo eureka server
2. EN LA CLASE PRINCIPAL main() agregar @EnableEurekaServer
3. CREAR ARCHIVO "application.yml" en la carpeta resources
	3.1
		- Contenido "application.yml"
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
server:
  port: 8761

===== GATEWAY SERVICE
1. CREAR PROYECTO CON GATEWAY
	1.1 Elegir Gateway, Spring Boot Actuator y Eureka Discovery Client
2. EN LA CLASE PRINCIPAL main() agregar @EnableEurekaClient y @EnableHystrix
3. CREAR ARCHIVO "application.yml" en la carpeta resources
	3.1
		- Contenido "application.yml" NOTA: quitar las comillas en el - Path=...
spring:
  application:
    name: GATEWAY-SERVICE
  cloud:
    gateway:
      routes:
        - id: order-service
          uri: lb://ORDER-SERVICE
          predicates:
            - Path="/order/**"
          // filters:
          //   - name: CircuitBreaker
          //     args:
          //       name: order-service
          //       fallbackuri: forward:/orderFallBack
        - id: payment-service
          uri: lb://PAYMENT-SERVICE
          predicates:
            - Path="/payment/**"
          // filters:
          //   - name: CircuitBreaker
          //     args:
          //       name: payment-service
          //       fallbackuri: forward:/paymentFallback
                

server:
  port: 8989
  
management:
  endpoints:
    web:
      exposure:
        include: hystrix.stream

hystrix:
  command:
    fallbackcmd:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 5000  

4. PARA QUE FUNCIONES CON HISTRIX DECOMENTAR EN "application.yml" y crear la clase
@RestController
public class FallbackController {

    @RequestMapping("/orderFallBack")
    public Mono<String> orderServiceFallBack() {
        return Mono.just("Order Service is taking too long to respond or is down. Please try again later");
    }
    @RequestMapping("/paymentFallback")
    public Mono<String> paymentServiceFallBack() {
        return Mono.just("Payment Service is taking too long to respond or is down. Please try again later");
    }
}

==== MICROSERVICE ALL CONFIG
1. CREAR PROYECTO CON Spring web, Spring Data JPa y si se necesita DB agregar MYSQL Driver. extra Lombok
2. EN LA CLASE PRINCIPAL main() agregar @EnableEurekaClient, SOLO CUANDO SE AGREGUE MS GATEWAY.
3. CREAR ARCHIVO "application.yml" en la carpeta resources
  3.1
    - Contenido "application.yml"
server:
	port: 9192
spring:
  // zipkin:
  //   base-url: http://localhost:9411/
  // h2:
  //   console:
  //     enabled: true
	application:
    	name: order-service
	datasource:
  		url: jdbc:mysql://localhost:3306/db_name
  		username: root
  		password: Exa_1987.
  		driver-class-name: com.mysql.cj.jdbc.Driver
  	jpa:
  		database-platform: org.hibernate.dialect.MySQL8Dialect
  	hibernate:
  		ddl-auto: none
  	// puesde ser create-drop o update, primero usar create-drop pasar a update o en none

logging:
level:
  	org:
  		hibernate:
  			SQL: debug
	file: C:/Users/path/name-service.log



====== ANGULAR CONSOLA =====
1. Descargar el Instalador de Windows
En primer lugar, es necesario descargar el archivo de instalación de Windows Installer (.msi) 
del sitio web oficial de Node.js. Esta base de datos del instalador MSI lleva una colección de
archivos de instalación esenciales para instalar, actualizar o modificar la versión existente de Node.js.

https://nodejs.org/en/download/

2. Instalar la CLI de Angular
Utilizarás la CLI de Angular para crear proyectos, generar código de aplicaciones y bibliotecas, 
y realizar una variedad de tareas de desarrollo, como pruebas, agrupación e implementación.
Para instalar CLI de Angular, abre una terminal y ejecuta el siguiente comando:

npm install -g @angular/cli

3. Crea un espacio de trabajo y una aplicación inicial
Desarrollas aplicaciones en el contexto de un espacio de trabajo de Angular.
Para crear un nuevo espacio de trabajo y una aplicación inicial:
Ejecuta el comando CLI ng new y proporciona el nombre my-app, como se muestra aquí:

ng new my-app

El comando ng new te solicitará información sobre las funciones que debe incluir en la aplicación inicial. 
Acepta los valores predeterminados presionando la tecla Enter o Return.
La CLI de Angular instala los paquetes npm de Angular necesarios y otras dependencias. 
Esto puede tardar unos minutos.
La CLI crea un nuevo espacio de trabajo y una aplicación de bienvenida simple, lista para ejecutarse.

Ejecutar la aplicación
La CLI de Angular incluye un servidor, de modo que puede crear y servir su aplicación localmente.
Navega a la carpeta del espacio de trabajo, como my-app.
Ejecuta el siguiente comando:

cd my-app
ng serve --open


====> EXTRAS ANGULAR

CREAR COMPONENTES
  ng g c components/header --skip-tests

Descripcion de comandos
g = generate
c = component
--skip-tests = omitir clases de test

CREAR MODELOS DE LOS DAOS
  ng g class models/categoria --skip-tests

Descripcion de comandos
g = generate
class = clases de tipo modelo dao
--skip-tests = omitir clases de test
Nota: CREAR CLASES DE ACUERDO A LOS TIPOS DE DAOS O MS A CONSUMIR...

CREAR SERVICIOS PARA CONSUMIR LOS MICROSERVICIOS
  ng g s services/categoria --skip-tests

Descripcion de comandos
g = generate
s = crea la clase para consumir los MICROSERVICIOS
--skip-tests = omitir clases de test
Nota: CREAR CLASES DE ACUERDO A LOS TIPOS DE DAOS O MS A CONSUMIR...