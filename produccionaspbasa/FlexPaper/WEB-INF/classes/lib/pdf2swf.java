package lib;

import java.io.File;
import javax.servlet.http.*;

public class pdf2swf extends common{

	HttpServletRequest request = null;

	public pdf2swf(HttpServletRequest request){
		this.request = request;
	}

	public String convert(String doc, String page, String ruta) {
		
		String path = ruta!=null?ruta:"";
		
		String pdfFilePath	= separate(getConfig("path.pdf", "")) + path + doc;
		String swfFilePath	= separate(getConfig("path.swf", "")) + path + doc + page + ".swf";

		String command		= getConfig("cmd.conversion.singledoc", "");
		if("true".equals(getConfig("splitmode", "")))
			command = getConfig("cmd.conversion.splitpages", "");

		command = command.replace("{path.pdf}", separate(getConfig("path.pdf", "")) + path);
		command = command.replace("{path.swf}", separate(getConfig("path.swf", "")) + path);
		command = command.replace("{pdffile}", doc);

		try {
			if (!isNotConverted(pdfFilePath ,swfFilePath)) {
				return "[Converted]";
			}
		} catch (Exception ex) {
			return "Error," + ex.toString();
		}

		boolean return_var = false;

		if("true".equals(getConfig("splitmode", ""))){
			String pagecmd = command.replace("%", page);
			pagecmd = pagecmd + " -p " + page;

			return_var = exec(pagecmd);
			int hash = getStringHashCode(command);
			HttpSession session = request.getSession(true);
			String constr = "CONVERSION_" + hash;
			String conversion = (String) session.getAttribute(constr);
            if(conversion == null){
                exec(command);
                session.setAttribute(constr, "true");
            }
		}else
			return_var = exec(command);
		String s = "Error converting document, make sure the conversion tool is installed and that correct user permissions are applied to the SWF Path directory" + 
					getDocUrl();
		if(return_var) {
			s="[Converted]";
		}
		return s;
	}

	public boolean isNotConverted(String pdfFilePath,String swfFilePath) throws Exception {
		File f = new File(pdfFilePath);
		if (!f.exists()) {
			throw new Exception("Document does not exist");
		}
		if (swfFilePath == null) {
			throw new Exception("Document output file name not set");
		} else {
			File s = new File(swfFilePath);
			if (!s.exists()) {
				return true;
			} else {
				if(f.lastModified() > s.lastModified()) return true;
			}
		}
		return false;
	}
}