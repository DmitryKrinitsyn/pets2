cqlsh -e "CREATE KEYSPACE IF NOT EXISTS bdssp WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 2};"
echo keyspace
cqlsh -e "USE bdssp;"
echo use
cqlsh -e "CREATE TABLE IF NOT EXISTS bdssp.filedata (id INT PRIMARY KEY, filename TEXT, filetext TEXT);"
echo table
cqlsh -e "CREATE CUSTOM INDEX IF NOT EXISTS lucene_index ON bdssp.filedata () \
                 USING 'com.stratio.cassandra.lucene.Index' \
                 WITH OPTIONS = { \
                     'refresh_seconds': '1', \
                     'schema': '{ \
                        fields: { \
                           filename: {type: \"string\"}, \
                           filetext: {type: \"string\"}  \
                            } \
                     }'\
                 };"
echo index