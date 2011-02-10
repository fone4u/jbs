/*
 * Copyright 2010 Internet Archive
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.lucene.document.*;
import org.apache.lucene.index.*;

/**
 * Custom FieldHandler implementation for site.
 *
 * The site field not stored and it is indexed as a single token.  In
 * addition, we apply some very rudimentary canonicalization, such as
 * stripping a leading 'www[0-9]' and a special rule for Photobucket.
 *
 * Ideally we would apply more sophisticated rules, perhaps even
 * collection-specific, to better determine what the "site" is for a
 * URL.
 */ 
public class SiteHandler implements FieldHandler
{
  IDNHelper helper;

  public SiteHandler( )
  {
    this( new IDNHelper( ) );
  }

  public SiteHandler( IDNHelper helper )
  {
    this.helper = helper;
  }

  public void handle( Document doc, DocumentProperties properties )
  {
    // Special handling for site
    try
      {
        URL u = new URL( properties.get( "url" ) );

        String domain = this.helper.getDomain( u );

        // If we cannot determine the domain, use the full hostname.
        // This can happen if the URL uses IP address rather than
        // hostname.
        if ( domain == null ) 
          {
            domain = u.getHost( );
          }
        else
          {
            domain = IDN.toUnicode( domain, IDN.ALLOW_UNASSIGNED );
          }

        doc.add( new Field( "site", domain, Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS) );
      }
    catch ( MalformedURLException mue )
      {
        // Rut-roh.
      }
  }

}
