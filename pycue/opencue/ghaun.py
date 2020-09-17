import cuebot
import compiled_proto.gcp_pb2 as g
import time
#import api

print("Firing Up cuebot")
openCue = cuebot.Cuebot
openCue.init()
gcp = openCue.getStub("gcp");
print("Cuebot has been initialized, creating a GCP Client")
client = g.GcpClient(project="ghaun-playbox", default_credentials=True)
print(gcp.getInstanceTemplateList(
    g.getInstanceTemplateListRequest(client=client)
));
#print(gcp.getZoneList(
#    g.getZoneListRequest(client=client)
#))
print("Client Created. Firing up Instance Group")
gcp.CreateManagedInstanceGroup(
    g.CreateManagedInstanceGroupRequest(
        instance_template="opencue-default-template",
        zone="/zones/us-central1-a",
        number_of_instances=20
    )
)
print("Instance Group Created. Will delete in 30 seconds")
time.sleep(30)
gcp.DeleteManagedInstanceGroup(
    g.DeleteManagedInstanceGroupRequest()
)
print("Instance Group Deleted")

#api.createService("gcp")