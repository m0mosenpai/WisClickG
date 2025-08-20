make:
	./gradlew war

clean:
	./gradlew clean

reset_db:
	@mongosh ResetMongo.js
