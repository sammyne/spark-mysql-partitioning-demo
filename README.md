# Spark MySQL Partitioning Demo

## Prerequisite
- mysql 8.0.28
- sbt 1.7.1
- scala 2.12.10
- spark 3.1.3

## Quickstart

```bash
# '192.168.10.10/alice/pairs' means load data from table 'pairs' in database 'pairs' of MySQL listening at 192.168.10.10
sbt "run 192.168.10.10/alice/pairs"
```

Log goes as
```bash
[info] compiling 1 Scala source to /github.com/sammyne/spark-mysql-partitioning-demo/target/scala-2.12/classes ...
[info] running (fork) com.github.sammyne.helloworld.HelloWorldMySQL 192.168.10.10/alice/pairs
[info] dbUri = 192.168.10.10/alice, tableName = pairs
[info] The dataframe contains 999 record(s) with 1 partition(s).
[info] +-----------+-----+
[info] |partitionId|count|
[info] +-----------+-----+
[info] |          0|  999|
[info] +-----------+-----+
[info] save output to 192.168.10.10/alice in table pairs_copy
[info] done :)
[success] Total time: 12 s, completed Jan 12, 2023 1:53:50 AM
```

indicating only 1 partitions is generated.

HOWEVER, WE ARE EXPECTING PARTITIONS COUNT TO BE 5.
