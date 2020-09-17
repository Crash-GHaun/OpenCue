#  Copyright Contributors to the OpenCue Project
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

"""
Project: opencue Library

Module: gcp.py - opencue Library implementation of gcp interface

"""

from opencue import Cuebot
from opencue.compiled_proto import gcp_pb2


class Gcp(object):

    project = "ghaun-playbox"
    default_credentials = True;

    def CreateGcpClient(self):
        """Tells the server to try GCP."""
        self.stub.CreateGcpClient(
            gcp_pb2.GcpCreateClientRequest(
                GcpClient=self),
            timeout=Cuebot.Timeout
            )