package com.rand10;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import com.rand10.util.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class ReverseText
 */
@WebServlet("/ReverseText")
public class ReverseText extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReverseText() {
        super();
        // TODO Auto-generated constructor stub
    }

    private File getFile(String filename)
    {
        String root = getServletContext().getRealPath("/");
        File path = new File(root + "/uploads");
        if (!path.exists()) {
            path.mkdirs();
        }

        return new File(path + "/" + filename);
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	String filename;
    	if((session != null) && (filename = (String)session.getAttribute("file")) != null)
    	{

    		response.setContentType("application/octet-stream");
    		File file = getFile(filename);
    		// This uses the name that was stored.  In the real world, it would be the user's name.
    		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

    		RandomAccessFile in = new RandomAccessFile(file,"r");
    		PrintWriter out = response.getWriter();
    		// horribly inefficient -- should use buffered reader/writer.
    		for(long i = in.length()-1;i>=0;i--)
    		{
    			in.seek(i);
    			out.write(in.read());	
    		}
    		in.close();
    		// get rid of temporary file and session reference -- it's been retrieved.  N
    		// ote this is pretty brain dead.  Does not
    		// allow user to try to re-download if browser failure, etc.
    		file.delete();
    		session.removeAttribute("file");
    	}else
    	{
    		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
    				"Sorry loser, you either already have your file or direct HTTP GET is not supported.");
    	}

    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (ServletFileUpload.isMultipartContent(request))
		{
			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			try
			{
				// Parse the request
				List /* FileItem */ items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				// Assumes there is only one file item and ignores the other form items.
				// Note: filename of uploaded file is used.  You should probably create a random name and
				// attach the user filename to that object for later retrieval (see doGet)
				while(iterator.hasNext()){
					FileItem item = (FileItem)iterator.next();
					if (!item.isFormField()) {
                        String fileName = item.getName();
                        File uploadedFile = getFile(fileName);
                        System.out.println(uploadedFile.getAbsolutePath());
                        item.write(uploadedFile);
                        HttpSession session = request.getSession();
                        String name = uploadedFile.getName();
                        session.setAttribute("file", name);
                    }
				}
				PrintWriter out = response.getWriter();
				out.println(ServletUtilities.headWithTitle("File submitted") +
						"<body>\n" +
						"<h2>File submitted</h2>" +
						"<p>Get your <a href=\""+request.getContextPath() + "/ReverseText\">file</a>\n" +
						"<p><a href=\""+request.getContextPath()+"\">Main menu</a>" +
						"</body></html>\n");
				
			}
			catch( FileUploadException e )
			{
				e.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}else
		{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Bad Request");

		}
	}

}
