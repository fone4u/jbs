
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;


public class SimpleFieldHandler implements FieldHandler
{
  String name;
  String key;
  Field.Store store;
  Field.Index index;

  public SimpleFieldHandler( String name, Field.Store store, Field.Index index )
  {
    this( name, name, store, index );
  }

  public SimpleFieldHandler( String name, String key, Field.Store store, Field.Index index )
  {
    this.name  = name;
    this.key   = key;
    this.store = store;
    this.index = index;
  }

  public void handle( Document doc, DocumentProperties properties )
  {
    doc.add( new Field( name, properties.get( key ), store, index ) );
  }

}