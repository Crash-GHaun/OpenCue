package com.imageworks.spcue.servant;

import java.io.IOException;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.compute.v1.DeleteInstanceGroupManagerHttpRequest;
import com.google.cloud.compute.v1.DeleteInstancesInstanceGroupManagerHttpRequest;
import com.google.cloud.compute.v1.InsertInstanceGroupManagerHttpRequest;
import com.google.cloud.compute.v1.InstanceGroupClient;
import com.google.cloud.compute.v1.InstanceGroupManager;
import com.google.cloud.compute.v1.InstanceGroupManagerClient;
import com.google.cloud.compute.v1.InstanceGroupManagerSettings;
import com.google.cloud.compute.v1.Operation;
import com.google.cloud.compute.v1.ProjectGlobalInstanceTemplateName;
import com.imageowrks.spcue.grpc.gcp.*;
import com.imageowrks.spcue.grpc.gcp.GcpInterfaceGrpc;
import com.imageowrks.spcue.grpc.gcp.GcpInterfaceGrpc.GcpInterfaceImplBase;
import com.imageworks.spcue.grpc.host.HostGetHostsResponse;

import com.imageworks.spcue.util.GcpUtil;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class ManageGCP extends GcpInterfaceGrpc.GcpInterfaceImplBase{
	
	private static final String PROJECT_ID = "ghaun-playbox";
	private static final String DEFAULT_INSTANCE_TEMPLATE_NAME = "opencue-default-template";
	private static final String DEFAULT_GROUP_NAME = "testing-group";
	private static FixedCredentialsProvider credentials; 

	@Override
	public void getInstanceTemplateList(getInstanceTemplateListRequest request,
				StreamObserver<com.imageowrks.spcue.grpc.gcp.getInstanceTemplateListResponse> responseObserver){
		// TODO (GHaun): Seriously change these system.out to logging
		System.out.println("Getting list of Instance Templates");
		try {
			responseObserver.onNext(
					getInstanceTemplateListResponse
							.newBuilder()
							.addAllTemplate(
									GcpUtil.getInstanceTemplateList(request.getClient().getProject()))
							.build()
			);
		}catch (IOException e){
			System.out.println(e);
			System.out.println("Failed to get Instance Template List");
		}
		responseObserver.onCompleted();
	}

	@Override
	public void getZoneList(getZoneListRequest request,
										StreamObserver<com.imageowrks.spcue.grpc.gcp.getZoneListResponse> responseObserver){
		// TODO (GHaun): Seriously change these system.out to logging
		System.out.println("Getting list of Instance Templates");
		try {
			responseObserver.onNext(
					getZoneListResponse
							.newBuilder()
							.addAllZone(
									GcpUtil.getZoneList(request.getClient().getProject()))
							.build()
			);
		}catch (IOException e){
			System.out.println(e);
			System.out.println("Failed to get Instance Template List");
		}
		responseObserver.onCompleted();
	}

	@Override
	public void createManagedInstanceGroup(CreateManagedInstanceGroupRequest request,
			StreamObserver<com.imageowrks.spcue.grpc.gcp.CreateManagedInstanceGroupResponse> responseObserver) {
		System.out.println("Checking Request");
		if (request.getInstanceTemplate() == null) {
			//need to throw error
			responseObserver.onCompleted();;
		}
		if (request.getZone() == null) {
			
		}
		if (request.getNumberOfInstances() < 1) {
			
		}
		try {
			GcpUtil.createManagedInstanceGroup(DEFAULT_GROUP_NAME, PROJECT_ID,
					ProjectGlobalInstanceTemplateName.of(
							request.getInstanceTemplate(), PROJECT_ID).toString(),
					(PROJECT_ID+request.getZone()), request.getNumberOfInstances());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseObserver.onError(e);
		}
		responseObserver.onNext(CreateManagedInstanceGroupResponse.newBuilder().build());
		responseObserver.onCompleted();
		
	}
	
	@Override
	public void deleteManagedInstanceGroup(DeleteManagedInstanceGroupRequest request,
			StreamObserver<com.imageowrks.spcue.grpc.gcp.DeleteManagedInstanceGroupResponse> responseObserver) {
		System.out.println("Deleting Instance Group");
		
		InstanceGroupManagerSettings igs;
		try {
			
			igs = InstanceGroupManagerSettings.newBuilder()
					.setCredentialsProvider(credentials)
					.build();
			InstanceGroupManagerClient igc = InstanceGroupManagerClient.create(igs);
			Operation response = igc.deleteInstanceGroupManager(DeleteInstanceGroupManagerHttpRequest.newBuilder()
					.setInstanceGroupManager(PROJECT_ID+"/zones/us-central1-a/instanceGroupManagers/"+DEFAULT_GROUP_NAME)
					.build());
			if (response.getError() == null) {
				System.out.println("Managed Instance Group is Deleted.");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		responseObserver.onNext(DeleteManagedInstanceGroupResponse.newBuilder().build());
		responseObserver.onCompleted();
	}

}
