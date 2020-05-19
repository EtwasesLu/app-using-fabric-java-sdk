package com.etwases.chaincode.invocation;


import com.liqi.mto.client.CAClient;
import com.liqi.mto.config.Config;
import com.liqi.mto.network.CreateChannel;
import com.liqi.mto.user.UserContext;
import com.liqi.mto.util.Util;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Etwases
 *
 */

public class QueryChaincodeByConfigFile {

	private static final String connectionProfilePath = System.getProperty("user.dir")
            + File.separator + "src" + File.separator + "main" + File.separator + "resources"
            + File.separator + "network-config.yaml";

	public static void main(String args[]) {


		try {
			//1、从ca服务获取
//            Util.cleanUp();
//			String caUrl = Config.CA_ORG1_URL;
//			CAClient caClient = new CAClient(caUrl, null);
//			// Enroll Admin to Org1MSP
//			UserContext adminUserContext = new UserContext();
//			adminUserContext.setName(Config.ADMIN);
//			adminUserContext.setAffiliation(Config.ORG1);
//			adminUserContext.setMspId(Config.ORG1_MSP);
//			caClient.setAdminUserContext(adminUserContext);
//			adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);
			UserContext org2Admin = new UserContext();
			File pkFolder2 = new File(Config.ORG2_USR_ADMIN_PK);
			File[] pkFiles2 = pkFolder2.listFiles();
			File certFolder2 = new File(Config.ORG2_USR_ADMIN_CERT);
			File[] certFiles2 = certFolder2.listFiles();
			Enrollment enrollOrg2Admin = Util.getEnrollment(Config.ORG2_USR_ADMIN_PK, pkFiles2[0].getName(),
					Config.ORG2_USR_ADMIN_CERT, certFiles2[0].getName());
			org2Admin.setEnrollment(enrollOrg2Admin);
			org2Admin.setMspId(Config.ORG2_MSP);
			org2Admin.setName(Config.ADMIN);


//			System.out.println(System.getProperty("user.dir"));
//			File f = new File(connectionProfilePath);
//			NetworkConfig networkConfig = NetworkConfig.fromYamlFile(f);
//			//2、从配置文件中获取用户信息
//          	NetworkConfig.OrgInfo orgInfo = networkConfig.getClientOrganization();
//			NetworkConfig.UserInfo userInfo = orgInfo.getPeerAdmin();

			//---------------
			CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
			// setup the client
			HFClient instance = HFClient.createNewInstance();
			instance.setCryptoSuite(cryptoSuite);
			instance.setUserContext(org2Admin);

//			Channel channel = instance.loadChannelFromConfig(Config.CHANNEL_NAME, networkConfig);
			Channel channel = instance.newChannel(Config.CHANNEL_NAME);
			Orderer orderer = instance.newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
			channel.addOrderer(orderer);
			channel.initialize();

			//将peer1_org1加入通道
			channel = instance.getChannel(Config.CHANNEL_NAME);
			Peer peer0_org2 = instance.newPeer(Config.ORG2_PEER_0, Config.ORG2_PEER_0_URL);
			Peer peer1_org2 = instance.newPeer(Config.ORG2_PEER_1, Config.ORG2_PEER_1_URL);

			channel.joinPeer(peer0_org2);
			channel.joinPeer(peer1_org2);

			Collection peers = channel.getPeers();
			Iterator peerIter = peers.iterator();
			while (peerIter.hasNext())
			{
				Peer pr = (Peer) peerIter.next();
				Logger.getLogger(CreateChannel.class.getName()).log(Level.INFO,pr.getName()+ " at " + pr.getUrl());
			}

			//在peer1_org1中安装链码


//			Logger.getLogger(QueryChaincodeByConfigFile.class.getName()).log(Level.INFO, "Querying for contract ...");
//
//			QueryByChaincodeRequest request = instance.newQueryProposalRequest();
//			ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
//			request.setChaincodeID(ccid);
//			request.setFcn("queryContract");
//			request.setArgs(new String[]{"contract1"});
//
//			Collection<ProposalResponse> responsesQuery = channel.queryByChaincode(request);
//
//			for (ProposalResponse pres : responsesQuery) {
//				String stringResponse = new String(pres.getChaincodeActionResponsePayload());
//				Logger.getLogger(QueryChaincodeByConfigFile.class.getName()).log(Level.INFO, stringResponse);
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
