import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;


public class Indexer
{
    private IndexWriter indexer;

    public Indexer(String indexDirectory) throws IOException
    {
        Directory directory = FSDirectory.open(Paths.get(indexDirectory));
        IndexWriterConfig configuration = new IndexWriterConfig();
        configuration.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        configuration.setCommitOnClose(true);

        indexer = new IndexWriter(directory, configuration);
    }

    public IndexWriter getIndexer()
    {
        return indexer;
    }

    public void buildIndex() throws IOException
    {
        FieldType token = new FieldType(StringField.TYPE_STORED);
        token.setStoreTermVectors(true);

        FieldType tokenize = new FieldType(TextField.TYPE_STORED);
        tokenize.setStoreTermVectors(true);

        for (HashMap.Entry<String, String> kv : FileOpener.getDirectories().entrySet())
        {
            try
            {
                System.out.println("indexing " + kv.getKey());

                Field path = new Field("path", kv.getKey(), token);
                Field url = new Field("url", kv.getValue(), tokenize);

                Document doc = new Document();
                doc.add(path);
                doc.add(url);

                for (HashMap.Entry<String, ArrayList<String>> entry : Parser.categorizeContent(Parser.parseHTML(kv.getKey())).entrySet())
                {
                    for (String text : entry.getValue())
                    {
                        Field content = new Field(entry.getKey(), text, tokenize);
                        doc.add(content);
                    }
                }

                indexer.addDocument(doc);
            }
            catch (IOException | IllegalArgumentException e)
            {
                e.printStackTrace();
            }
        }

        indexer.commit();
    }
}