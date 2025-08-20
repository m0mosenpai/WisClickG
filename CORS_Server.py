"""
File: CORS_Server.py
Author: Amelia Sitzberger
Date: April 2024
Description: Creates a simple server that will send response headers configured with CORS
		This server prevents CORS errors from occurring such as the response not shown in the user's browser.
"""

# Creates and runs server with CORS configurations
#!/usr/bin/env python3
# encoding: utf-8

from http.server import HTTPServer, SimpleHTTPRequestHandler


class CORSRequestHandler(SimpleHTTPRequestHandler):
    def end_headers(self):
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', '*')
        return super(CORSRequestHandler, self).end_headers()


httpd = HTTPServer(('localhost', 8000), CORSRequestHandler)

try:
	httpd.serve_forever()
except KeyboardInterrupt:
	pass
finally:
	httpd.server_close()
