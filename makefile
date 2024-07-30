VERSION := $(shell mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
DOCKER_IMAGE_TAG := ${VERSION}
DOCKER_CONTAINER_BASE_NAME := schimidt/observability
DOCKER_CONTAINER_IMAGE := ${DOCKER_CONTAINER_BASE_NAME}:${DOCKER_IMAGE_TAG}
DOCKER_CONTAINER_LATEST := ${DOCKER_CONTAINER_BASE_NAME}:latest
IS_RUNNING_ALL_COMMAND?=false
.DEFAULT_GOAL := clean-and-run-all

build:
	echo "Building version $(VERSION)"
	docker build -f Dockerfile --build-arg APP_VERSION="$(DOCKER_IMAGE_TAG)" -t $(DOCKER_CONTAINER_IMAGE) .
	@docker tag $(DOCKER_CONTAINER_IMAGE) schimidt/observability:latest
	@echo "Docker images $(DOCKER_CONTAINER_IMAGE) and $(DOCKER_CONTAINER_LATEST) built."

stop-all: IS_RUNNING_ALL_COMMAND=true

stop-all: stop
	@echo "Deleting Alert Manager config ..."
	@rm monitoring/alertmanager/alertmanager.yml >/dev/null 2>&1 || true
	@docker-compose stop >/dev/null 2>&1 || true
	@echo "All docker containers stopped."

clean-all: IS_RUNNING_ALL_COMMAND=true

clean-all: stop-all clean
	@docker-compose down >/dev/null 2>&1
	@echo "All docker containers removed."

run-all: build
	@echo "Generating Alert Manager config with Slack Api Webhook ..."
	@envsubst < monitoring/alertmanager/alertmanager.template.yml > monitoring/alertmanager/alertmanager.yml
	@DOCKER_CONTAINER_IMAGE=$(DOCKER_CONTAINER_IMAGE) SLACK_API_URL=$(SLACK_API_URL) docker-compose up -d
	@echo "Docker app started with image $(DOCKER_CONTAINER_IMAGE)."

clean-and-run-all: clean-all run-all

stop:
	@docker stop app >/dev/null 2>&1 || true
	@if [ "${IS_RUNNING_ALL_COMMAND}" = false ]; then \
		echo "Docker container app stopped."; \
	fi

clean: stop
	@docker rm -f app >/dev/null 2>&1 || true
	@if [ "${IS_RUNNING_ALL_COMMAND}" = false ]; then \
  		echo "Docker container app removed."; \
	fi

run: build
	@docker run -d --rm --name app -p 8080:8080 $(DOCKER_CONTAINER_IMAGE)
	@echo "Docker container app started."

clean-and-run: clean run

clean-db:
	@echo "Cleaning up database ..."
	@docker	stop postgres >/dev/null 2>&1 || true
	@docker	rm postgres >/dev/null 2>&1 || true

run-db:
	@echo "Starting database ..."
	@docker-compose	up -d postgres >/dev/null 2>&1 || true

clean-and-run-db: clean-db run-db

logs:
	@docker logs -f app

logs-all:
	@docker-compose logs -f

ps:
	@docker ps

sh:
	@docker exec -it app sh

# Phony targets
.PHONY: build clean-all stop-all run-all clean-and-run-all stop clean run clean-and-run logs ps sh

