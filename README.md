# spring-config-server

getting properties from vault and git example

  {
  "name": "demo-api-a",
  "profiles": [
    "dev"
  ],
  "label": "master",
  "version": null,
  "state": null,
  "propertySources": [
    {
      "name": "vault:demo-api-a,dev",
      "source": {
        "data.git.vault.password": "demopassword",
        "data.git.vault.username": "SumitBhawsar",
        "metadata.created_time": "2021-03-02T09:12:05.1789301Z",
        "metadata.deletion_time": "",
        "metadata.destroyed": false,
        "metadata.version": 2
      }
    },
    {
      "name": "vault:demo-api-a",
      "source": {
        "data.git.vault.password": "demopassword",
        "data.git.vault.username": "SumitBhawsar",
        "metadata.created_time": "2021-03-02T08:24:28.7637773Z",
        "metadata.deletion_time": "",
        "metadata.destroyed": false,
        "metadata.version": 1
      }
    },
    {
      "name": "https://github.com/SumitBhawsar/demo-api-a-config.git/demo-api-a-dev.properties",
      "source": {
        "api.name": "demo-api-a",
        "api.profile": "dev",
        "config-repo.name": "demo-api-a-config",
        "demo-api.prop1": "value1",
        "demo-api.version": "1.0"
      }
    },
    {
      "name": "https://github.com/SumitBhawsar/demo-api-a-config.git/application-dev.properties",
      "source": {
        "api.name": "demo-api-a",
        "api.profile": "dev",
        "api.file": "application-dev.properties",
        "config-repo.name": "demo-api-a-config",
        "demo-api.prop1": "value1",
        "demo-api.version": "1.0"
      }
    }
  ]
}
