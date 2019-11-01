package com.qx.web.mime;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author pc
 *
 */
public enum MIME_TopType {


	/**
	 * <p>
	 * The "text" top-level type is intended for sending material that is
	 * principally textual in form.
	 * </p>
	 * <p>
	 * Many subtypes of text, notably including the subtype "text/plain", which is a
	 * generic subtype for plain text defined in [RFC2046], define a "charset"
	 * parameter. If a "charset" parameter is defined for a particular subtype of
	 * text, it MUST be used to specify a charset name defined in accordance to the
	 * procedures laid out in [RFC2978].
	 * </p>
	 * <p>
	 * As specified in [RFC6657], a "charset" parameter SHOULD NOT be specified when
	 * charset information is transported inside the payload (e.g., as in
	 * "text/xml").
	 * </p>
	 * <p>
	 * If a "charset" parameter is specified, it SHOULD be a required parameter,
	 * eliminating the options of specifying a default value. If there is a strong
	 * reason for the parameter to be optional despite this advice, each subtype MAY
	 * specify its own default value, or alternatively, it MAY specify that there is
	 * no default value. Finally, the "UTF-8" charset [RFC3629] SHOULD be selected
	 * as the default. See [RFC6657] for additional information on the use of
	 * "charset" parameters in conjunction with subtypes of text.
	 * </p>
	 * <p>
	 * Regardless of what approach is chosen, all new text/* registrations MUST
	 * clearly specify how the charset is determined; relying on the US-ASCII
	 * default defined in Section 4.1.2 of [RFC2046] is no longer permitted. If
	 * explanatory text is needed, this SHOULD be placed in the additional
	 * information section of the registration.
	 * </p>
	 * <p>
	 * Plain text does not provide for or allow formatting commands, font attribute
	 * specifications, processing instructions, interpretation directives, or
	 * content markup. Plain text is seen simply as a linear sequence of characters,
	 * possibly interrupted by line breaks or page breaks. Plain text MAY allow the
	 * stacking of several characters in the same position in the text. Plain text
	 * in scripts like Arabic and Hebrew may also include facilities that allow the
	 * arbitrary mixing of text segments with different writing directions.
	 * </p>
	 * <p>
	 * Beyond plain text, there are many formats for representing what might be
	 * known as "rich text". An interesting characteristic of many such
	 * representations is that they are to some extent readable even without the
	 * software that interprets them. It is useful to distinguish them, at the
	 * highest level, from such unreadable data as images, audio, or text
	 * represented in an unreadable form. In the absence of appropriate
	 * interpretation software, it is reasonable to present subtypes of "text" to
	 * the user, while it is not reasonable to do so with most non-textual data.
	 * Such formatted textual data can be represented using subtypes of "text".
	 * </p>
	 */
	TEXT("text"),

	/**
	 * <p>
	 * A top-level type of "image" indicates that the content specifies one or more
	 * individual images. The subtype names the specific image format.
	 * </p>
	 */
	IMAGE("image"),

	/**
	 * <h1>Audio Media Types</h1>
	 * <p>
	 * A top-level type of "audio" indicates that the content contains audio data.
	 * The subtype names the specific audio format.
	 * </p>
	 */
	AUDIO("audio"),


	/**
	 * <p>
	 * A top-level type of "video" indicates that the content specifies a
	 * time-varying-picture image, possibly with color and coordinated sound. The
	 * term 'video' is used in its most generic sense, rather than with reference to
	 * any particular technology or format, and is not meant to preclude subtypes
	 * such as animated drawings encoded compactly.
	 * </p>
	 * <p>
	 * Note that although in general the mixing of multiple kinds of media in a
	 * single body is discouraged [RFC2046], it is recognized that many video
	 * formats include a representation for synchronized audio and/or text, and this
	 * is explicitly permitted for subtypes of "video".
	 * </p>
	 */
	VIDEO("video"),


	/**
	 * <h1>Application Media Types</h1>
	 * <p>
	 * 
	 * The "application" top-level type is to be used for discrete data that do not
	 * fit under any of the other type names, and particularly for data to be
	 * processed by some type of application program. This is information that must
	 * be processed by an application before it is viewable or usable by a user.
	 * Expected uses for the "application" type name include but are not limited to
	 * file transfer, spreadsheets, presentations, scheduling data, and languages
	 * for "active" (computational) material. (The last, in particular, can pose
	 * security problems that must be understood by implementors. The
	 * "application/postscript" media type registration in [RFC2046] provides a good
	 * example of how to handle these issues.)
	 * </p>
	 * <p>
	 * 
	 * For example, a meeting scheduler might define a standard representation for
	 * information about proposed meeting dates. An intelligent user agent would use
	 * this information to conduct a dialog with the user, and might then send
	 * additional material based on that dialog. More generally, there have been
	 * several "active" languages developed in which programs in a suitably
	 * specialized language are transported to a remote location and automatically
	 * run in the recipient's environment. Such applications may be defined as
	 * subtypes of the "application" top-level type.
	 * </p>
	 * <p>
	 * The subtype of "application" will often either be the name or include part of
	 * the name of the application for which the data are intended. This does not
	 * mean, however, that any application program name may simply be used freely as
	 * a subtype of "application"; the subtype needs to be registered.
	 * </p>
	 */
	APPLICATION("application"),



	/**
	 * <h1>RFC 2046/5.1. Multipart Media Type</h1>
	 * <p>
	 * In the case of multipart entities, in which one or more different sets of
	 * data are combined in a single body, a "multipart" media type field must
	 * appear in the entity's header. The body must then contain one or more body
	 * parts, each preceded by a boundary delimiter line, and the last one followed
	 * by a closing boundary delimiter line. After its boundary delimiter line, each
	 * body part then consists of a header area, a blank line, and a body area. Thus
	 * a body part is similar to an RFC 822 message in syntax, but different in
	 * meaning.
	 * </p>
	 * <p>
	 * A body part is an entity and hence is NOT to be interpreted as actually being
	 * an RFC 822 message. To begin with, NO header fields are actually required in
	 * body parts. A body part that starts with a blank line, therefore, is allowed
	 * and is a body part for which all default values are to be assumed. In such a
	 * case, the absence of a Content-Type header usually indicates that the
	 * corresponding body has a content-type of "text/plain; charset=US-ASCII".
	 * </p>
	 * <p>
	 * The only header fields that have defined meaning for body parts are those the
	 * names of which begin with "Content-". All other header fields may be ignored
	 * in body parts. Although they should generally be retained if at all possible,
	 * they may be discarded by gateways if necessary. Such other fields are
	 * permitted to appear in body parts but must not be depended on. "X-" fields
	 * may be created for experimental or private purposes, with the recognition
	 * that the information they contain may be lost at some gateways.
	 * </p>
	 * <p>
	 * NOTE: The distinction between an RFC 822 message and a body part is subtle,
	 * but important. A gateway between Internet and X.400 mail, for example, must
	 * be able to tell the difference between a body part that contains an image and
	 * a body part that contains an encapsulated message, the body of which is a
	 * JPEG image. In order to represent the latter, the body part must have
	 * "Content-Type: message/rfc822", and its body (after the blank line) must be
	 * the encapsulated message, with its own "Content-Type: image/jpeg" header
	 * field. The use of similar syntax facilitates the conversion of messages to
	 * body parts, and vice versa, but the distinction between the two must be
	 * understood by implementors. (For the special case in which parts actually are
	 * messages, a "digest" subtype is also defined.)
	 * </p>
	 * <p>
	 * As stated previously, each body part is preceded by a boundary delimiter line
	 * that contains the boundary delimiter. The boundary delimiter MUST NOT appear
	 * inside any of the encapsulated parts, on a line by itself or as the prefix of
	 * any line. This implies that it is crucial that the composing agent be able to
	 * choose and specify a unique boundary parameter value that does not contain
	 * the boundary parameter value of an enclosing multipart as a prefix.
	 * </p>
	 * <p>
	 * All present and future subtypes of the "multipart" type must use an identical
	 * syntax. Subtypes may differ in their semantics, and may impose additional
	 * restrictions on syntax, but must conform to the required syntax for the
	 * "multipart" type. This requirement ensures that all conformant user agents
	 * will at least be able to recognize and separate the parts of any multipart
	 * entity, even those of an unrecognized subtype.
	 * </p>
	 * <p>
	 * As stated in the definition of the Content-Transfer-Encoding field [RFC
	 * 2045], no encoding other than "7bit", "8bit", or "binary" is permitted for
	 * entities of type "multipart". The "multipart" boundary delimiters and header
	 * fields are always represented as 7bit US-ASCII in any case (though the header
	 * fields may encode non-US-ASCII header text as per RFC 2047) and data within
	 * the body parts can be encoded on a part-by-part basis, with
	 * Content-Transfer-Encoding fields for each appropriate body part.
	 * </p>
	 */
	MULTIPART("multipart"),


	/**
	 * <h1>rfc2046/5.2. Message Media Type</h1>
	 * 
	 * <p>
	 * 
	 * It is frequently desirable, in sending mail, to encapsulate another mail
	 * message. A special media type, "message", is defined to facilitate this. In
	 * particular, the "rfc822" subtype of "message" is used to encapsulate RFC 822
	 * messages.
	 * </p>
	 * <p>
	 * NOTE: It has been suggested that subtypes of "message" might be defined for
	 * forwarded or rejected messages. However, forwarded and rejected messages can
	 * be handled as multipart messages in which the first part contains any control
	 * or descriptive information, and a second part, of type "message/rfc822", is
	 * the forwarded or rejected message. Composing rejection and forwarding
	 * messages in this manner will preserve the type information on the original
	 * message and allow it to be correctly presented to the recipient, and hence is
	 * strongly encouraged.
	 * </p>
	 * <p>
	 * Subtypes of "message" often impose restrictions on what encodings are
	 * allowed. These restrictions are described in conjunction with each specific
	 * subtype.
	 * </p>
	 * <p>
	 * Mail gateways, relays, and other mail handling agents are commonly known to
	 * alter the top-level header of an RFC 822 message. In particular, they
	 * frequently add, remove, or reorder header fields. These operations are
	 * explicitly forbidden for the encapsulated headers embedded in the bodies of
	 * messages of type "message."
	 * </p>
	 */
	MESSAGE("message");


	private static Map<String, MIME_TopType> TOP_TYPES;

	private static boolean isInitialized;

	public String template;

	private MIME_TopType(String template) {
		this.template = template;
	}


	public static MIME_TopType get(String template) {
		if(!isInitialized) {
			TOP_TYPES = new HashMap<>();
			for(MIME_TopType topType : MIME_TopType.values()) {
				TOP_TYPES.put(topType.template, topType);
			}
			isInitialized = true;
		}
		return TOP_TYPES.get(template);
	}

}
