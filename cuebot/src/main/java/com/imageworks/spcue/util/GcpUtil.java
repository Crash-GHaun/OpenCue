package com.imageworks.spcue.util;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.compute.v1.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class GcpUtil {

    private static FixedCredentialsProvider getCredentials() throws IOException{
        // TODO(GHaun): Figure out if it's ok to store credentials once, or if it's good practice to create them everytime
        return FixedCredentialsProvider.create(
                GoogleCredentials.getApplicationDefault()
        );
    }

    public static void createManagedInstanceGroup(String groupName, String projectID,
                                           String instanceTemplate, String zone,
                                           int maxInstances) throws IOException{
        System.out.println("Creating Managed Instance Group");
        //Configure Instance Settings
        InstanceGroupManagerSettings igs = InstanceGroupManagerSettings.newBuilder()
                .setCredentialsProvider(getCredentials())
                .build();
        InstanceGroupManagerClient igc = InstanceGroupManagerClient.create(igs);

        InsertInstanceGroupManagerHttpRequest r = InsertInstanceGroupManagerHttpRequest.newBuilder()
                .setInstanceGroupManagerResource(InstanceGroupManager.newBuilder()
                        .setName(groupName)
                        .setInstanceTemplate(ProjectGlobalInstanceTemplateName.of(
                                instanceTemplate, projectID).toString())
                        .setTargetSize(maxInstances)
                        .build())
                .setZone(zone)
                .build();

        Operation response = igc.insertInstanceGroupManager(r);
        if (response.getError() == null) {
            System.out.println("Managed Instance Group is Created.");
        }
    }

    public static void deleteManagedInstanceGroup(String PROJECT_ID)throws IOException{
        InstanceGroupManagerSettings igs = InstanceGroupManagerSettings.newBuilder()
                .setCredentialsProvider(getCredentials())
                .build();
        InstanceGroupManagerClient igc = InstanceGroupManagerClient.create(igs);
        Operation response = igc.deleteInstanceGroupManager(DeleteInstanceGroupManagerHttpRequest.newBuilder()
                .setInstanceGroupManager(PROJECT_ID+"/zones/us-central1-a/instanceGroupManagers/")
                .build());
        if (response.getError() == null) {
            System.out.println("Managed Instance Group is Deleted.");
        }
    }

    public static List<String> getInstanceTemplateList(String project_id) throws IOException{
        List<String> templates = new ArrayList<String>();
        InstanceTemplateClient client = InstanceTemplateClient.create();
        ListInstanceTemplatesHttpRequest request = ListInstanceTemplatesHttpRequest.newBuilder()
                .setProject(project_id).build();
        for (InstanceTemplate element : client.listInstanceTemplates(request).iterateAll()) {
            templates.add(element.getName());
        }
        return templates;
    }

    public static List<String> getZoneList(String project_id) throws IOException{
        List<String> zones = new ArrayList<String>();
        ZoneClient client = ZoneClient.create();
        for (Zone z : client.listZones(project_id).iterateAll()){
            zones.add(project_id+"/zones/"+z.getName());
        }
        return zones;
    }

    public static void main(String[] args){
        try {
            getZoneList("ghaun-playbox");
        }catch (IOException e){

        }
    }
}
