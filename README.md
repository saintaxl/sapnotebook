# Notebook
This is a Java study notes project that primarily utilizes new features of the JDK and design patterns to create examples based on cloud-native concepts. It mainly covers microservices, monitoring, containerization, and service orchestration.

# Restful Controller

```java
@RestController
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to my Spring Boot application!";
    }

    @GetMapping("/helloworld")
    public String getHelloWorld() {
        return "Hello World";
    }

}
```

# Github Tokener Services

```java
public String getInstallationId(String jwt) throws IOException {
        URL url = new URL("https://api.github.com/app/installations");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + jwt);
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            // Bussiness Code
            return rootNode.get(0).get("id").asText();
        } else {
            throw new RuntimeException("Failed to get installation ID: " + responseCode);
        }
    }

```

# Functional
```java
@FunctionalInterface
public interface MyInterface {
    void show();
}

public class MyInterfaceDemo {
    public static void main(String[] args) {
        MyInterface my = () -> System.out.println("Functional Interface");
        my.show();
    }
}
```
