detekt:
	./gradlew detekt

test:
	./gradlew test

check: detekt test

doc:
	./gradlew dokkaHtml

release:
	./gradlew clean assemble publishToSonatype closeAndReleaseSonatypeStagingRepository
