package gui;

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
import org.bson.types.ObjectId;

import infsysProj.infsysProj.ConferenceEdition;
import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Proceedings;
import infsysProj.infsysProj.Publisher;
import infsysProj.infsysProj.Series;

public class ProceedingsCodec implements CollectibleCodec<Proceedings>{


	private Codec<Document> documentCodec;

	public ProceedingsCodec() {
		this.documentCodec = new DocumentCodec();
	}

	public ProceedingsCodec(Codec<Document> codec) {
		this.documentCodec = codec;
	}

	@Override
	public void encode(BsonWriter writer, Proceedings value,
			EncoderContext encoderContext) {
		Document document = new Document();

		String id = value.getId();
		String note = value.getNote();
		Integer number = value.getNumber();
		Publisher publisher = value.getPublisher();
		String volume = value.getVolume();
		String isbn = value.getIsbn();
		Series series = value.getSeries();
		ConferenceEdition confEdition = value.getConferenceEdition();
		Set<InProceedings> inProceedings = value.getInProceedings();
		if (null != id) {
			document.put("_id", id);
		}
		if (null != note) {
			document.put("note", note);
		}
		if (null != number) {
			document.put("number", number);
		}
		if (null != publisher) {
			document.put("publisher", publisher);
		}
		if (null != volume) {
			document.put("volume", volume);
		}
		if (null != isbn) {
			document.put("isbn", isbn);
		}
		if (null != series) {
			document.put("series", series);
		}
		if (null != confEdition) {
			document.put("confEdition", confEdition);
		}
		if (null != inProceedings) {
			document.put("inProceedings", inProceedings);
		}

		documentCodec.encode(writer, document, encoderContext);

	}

	@Override
	public Class<Proceedings> getEncoderClass() {
		return Proceedings.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Proceedings decode(BsonReader reader, DecoderContext decoderContext) {
		Document document = documentCodec.decode(reader, decoderContext);
		System.out.println("document "+document);
		Proceedings proc = new Proceedings();

		proc.setNote(document.getString("note"));

		proc.setNumber(document.getInteger("number"));

		proc.setId(document.getString("_id"));

		proc.setIsbn(document.getString("isbn"));
		
		proc.setPublisher((Publisher) document.get("publisher"));
		
		proc.setVolume(document.getString("volume"));
		
		proc.setSeries((Series) document.get("series"));		
		
		proc.setConferenceEdition((ConferenceEdition) document.get("conferenceEdition"));
		
		proc.setInProceedings((Set<InProceedings>) document.get("inProceedings"));
		
		return proc;
	}

	@Override
	public Proceedings generateIdIfAbsentFromDocument(Proceedings document) {
		if (document != null && !documentHasId(document)) {
			document.setId("id1");
		}
		return document;
	}

	@Override
	public boolean documentHasId(Proceedings document) {
		return null == document.getId();
	}

	@Override
	public BsonValue getDocumentId(Proceedings document) {
		if (!documentHasId(document))
	    {
	        throw new IllegalStateException("The document does not contain an _id");
	    }
	 
	    return new BsonString(document.getId());
}
}
