package gui;

import java.util.HashSet;
import java.util.Set;

import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;

import infsysProj.infsysProj.ConferenceEdition;
import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Proceedings;
import infsysProj.infsysProj.Publication;
import infsysProj.infsysProj.Publisher;
import infsysProj.infsysProj.Series;

public class PublisherCode implements CollectibleCodec<Publisher>{


		private Codec<Document> documentCodec;

		public PublisherCode() {
			this.documentCodec = new DocumentCodec();
		}

		public PublisherCode(Codec<Document> codec) {
			this.documentCodec = codec;
		}
		String name;
		Set<Publication> publications = new HashSet<Publication>();
		@Override
		public void encode(BsonWriter writer, Publisher value,
				EncoderContext encoderContext) {
			Document document = new Document();

			String id = value.getId();
			String name = value.getName();
			Set<Publication> publications = value.getPublications();

			if (null != id) {
				document.put("_id", id);
			}
			if (null != name) {
				document.put("name", name);
			}
			if (null != publications) {
				document.put("publications", publications);
			}

			documentCodec.encode(writer, document, encoderContext);

		}

		@Override
		public Class<Publisher> getEncoderClass() {
			return Publisher.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Publisher decode(BsonReader reader, DecoderContext decoderContext) {
			Document document = documentCodec.decode(reader, decoderContext);
			System.out.println("document "+document);
			Publisher pub = new Publisher();

			pub.setName(document.getString("name"));

			pub.setPublications((Set<Publication>) document.get("publications"));
			
			return pub;
		}

		@Override
		public Publisher generateIdIfAbsentFromDocument(Publisher document) {
			if (document != null && !documentHasId(document)) {
				document.setId("id1");
			}
			return document;
		}

		@Override
		public boolean documentHasId(Publisher document) {
			return null == document.getId();
		}

		@Override
		public BsonValue getDocumentId(Publisher document) {
			if (!documentHasId(document))
		    {
		        throw new IllegalStateException("The document does not contain an _id");
		    }
		 
		    return new BsonString(document.getId());
	}
	}

