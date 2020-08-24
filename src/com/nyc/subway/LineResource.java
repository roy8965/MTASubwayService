package com.nyc.subway;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class LineResource {
	
	@GET
	@Path("/status/{line}")
	@Produces(MediaType.TEXT_PLAIN)
	public String lineStatus(@PathParam("line") String line) {
		String status = "Line " + line + " is " + LineUtils.getLineStatus(line);
		return status;
	}
	
	@GET
	@Path("/status/{line}")
	@Produces(MediaType.TEXT_HTML)
	public String lineStatusHTML(@PathParam("line") String line) {
		String status = LineUtils.getLineStatus(line);	
		return "<html><title>MTA Train Status</title>"
				+ "<body><h1>Line " + line + " is " + status + "</h1><body></html>";
	}
	
	@GET
	@Path("/uptime/{line}")
	@Produces(MediaType.TEXT_PLAIN)
	public String lineUptime(@PathParam("line") String line) {
		String status = "The uptime of line " + line + " is " + LineUtils.getUptime(line);
		return status;
	}
	
	@GET
	@Path("/uptime/{line}")
	@Produces(MediaType.TEXT_HTML)
	public String lineUptimeHTML(@PathParam("line") String line) {
		String status = LineUtils.getUptime(line);	
		return "<html><title>MTA Train Status</title>"
				+ "<body><h1>The uptime of line " + line + " is " + status + "</h1><body></html>";
	}
	
	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_HTML)
	public String testLineStatusHTML() {
		return "<html><title>MTA Train Status</title>"
				+ "<body><h1>Line 7 is s experiencing delays</h1><body></html>";
	}
	
	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	public LineInfo testLineStatusJSON() {
		return new LineInfo("7", "not delayed");
	}

}
