KDB_CONTAINER_IMAGE=local/kdb

.PHONY: build_kdb_container
build_kdb_container:
	cd kdb-container; docker build -t $(KDB_CONTAINER_IMAGE):1.0.0 .