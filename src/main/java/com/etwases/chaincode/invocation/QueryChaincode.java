package com.etwases.chaincode.invocation;


import com.liqi.mto.client.CAClient;
import com.liqi.mto.client.ChannelClient;
import com.liqi.mto.client.FabricClient;
import com.liqi.mto.config.Config;
import com.liqi.mto.user.UserContext;
import com.liqi.mto.util.Util;
import org.hyperledger.fabric.sdk.*;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 
 * @author Etwases
 *
 */

public class QueryChaincode {

	private static final byte[] EXPECTED_EVENT_DATA = "!".getBytes(UTF_8);
	private static final String EXPECTED_EVENT_NAME = "event";

	public static void main(String args[]) {
		try {
            Util.cleanUp();
			String caUrl = Config.CA_ORG1_URL;
			CAClient caClient = new CAClient(caUrl, null);
			// Enroll Admin to Org1MSP
			UserContext adminUserContext = new UserContext();
			adminUserContext.setName(Config.ADMIN);
			adminUserContext.setAffiliation(Config.ORG1);
			adminUserContext.setMspId(Config.ORG1_MSP);
			caClient.setAdminUserContext(adminUserContext);
			adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);
			
			FabricClient fabClient = new FabricClient(adminUserContext);
			
			ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
			Channel channel = channelClient.getChannel();
			Peer peer = fabClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
			EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://192.168.0.208:7053");
			Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
			channel.addPeer(peer);
			channel.addEventHub(eventHub);
			channel.addOrderer(orderer);
			channel.initialize();

			Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, "Querying for all cars ...");
			Collection<ProposalResponse>  responsesQuery = channelClient.queryByChainCode("contract", "queryContract", new String[]{"contract1"});
			for (ProposalResponse pres : responsesQuery) {
				String stringResponse = new String(pres.getChaincodeActionResponsePayload());
				Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, stringResponse);
			}

//			Thread.sleep(10000);
//			String[] args1 = {"CAR1"};
//			Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, "Querying for a car - " + args1[0]);
//
//			Collection<ProposalResponse>  responses1Query = channelClient.queryByChainCode("fabcar", "queryCar", args1);
//			for (ProposalResponse pres : responses1Query) {
//				String stringResponse = new String(pres.getChaincodeActionResponsePayload());
//				Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, stringResponse);
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
