
# Cloud events examples
> **cloud-events-examples** are examples that demonstrates the use of cloud events in java, spring, etc.

## Getting started
To get a local copy up and running follow these simple example steps.

### Prerequisites

Cloud events spec docs: https://github.com/cloudevents/spec/blob/v1.0.2/cloudevents/spec.md
https://repo.maven.apache.org/maven2/io/cloudevents/cloudevents-http-basic/
`mvn exec:java -Dexec.mainClass="com.mtanevski.cloudevents.examples.kafka.SampleProducer" -Dexec.args="localhost:9092 sample-topic"`
This section will describe requirements needed to satisfy the installation or running of the project.

- Java 17
- Maven
- Docker
- Sample Kafka instance: E.g. `docker run --rm --net=host -e ADV_HOST=localhost -e SAMPLEDATA=0 lensesio/fast-data-dev`

### Nice to have
- Intellij

### Build
- `mvn clean install`

### Test
- Run `mvn test`

## Contact

For contact, you can reach me at [marjantanevski@outlook.com](marjantanevski@outlook.com).

## License

MIT © [Marjan Tanevski](marjantanevski@outlook.com)