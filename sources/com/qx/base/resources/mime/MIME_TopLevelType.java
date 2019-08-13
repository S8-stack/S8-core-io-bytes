package com.qx.base.resources.mime;



/**
 * <h1>From Request for Comments: 6838, Media Type Specifications and Registration Procedures<h1>
 * <p>top types definition </p>
 * @author pc
 *
 */
public enum MIME_TopLevelType {

	
	/**
	 * <p>
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
	 * <p>
	 * Multipart and message are composite types; that is, they provide a means of
	 * encapsulating zero or more objects, each one a separate media type.
	 * </p>
	 * <p>
	 * All subtypes of multipart and message MUST conform to the syntax rules and
	 * other requirements specified in [RFC2046] and amended by Section 3.5 of
	 * [RFC6532].
	 * </p>
	 */
	APPLICATION("application"),
	
	/**
	 * A top-level type of "audio" indicates that the content contains audio data.
	 * The subtype names the specific audio format.
	 */
	AUDIO("audio"),
	EXAMPLE("example"),
	FONT("font"),
	
	/**
	 * A top-level type of "image" indicates that the content specifies one or more
	 * individual images. The subtype names the specific image format.
	 */
	IMAGE("image"),
	
	MESSAGE("message"),
	MODEL("model"),
	MULTIPART("multipart"),
	
	
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
	VIDEO("video");

	public String tag;

	private MIME_TopLevelType(String tag) {
		this.tag = tag;
	}

}
