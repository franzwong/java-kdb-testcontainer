# Integration test of Java and kdb+ with testcontainer

## Create kdb+ docker container

1. Download `l64.zip` from Kx to `${PROJECT_DIR}/kdb-container/q`
2. Extract `l64.zip`
3. Put license file as `${PROJECT_DIR}/kdb-container/kc.lic`

Folder structure of `${PROJECT_DIR}/kdb-container` should be like this.

```
.
├── Dockerfile
├── kc.lic
└── q
    ├── l64
    │   └── q
    └── q.k
```

4. Build docker image

```bash
cd ${PROJECT_DIR}
make build_kdb_container
```

## Run test

```bash
cd ${PROJECT_DIR}
mvn clean verify
```