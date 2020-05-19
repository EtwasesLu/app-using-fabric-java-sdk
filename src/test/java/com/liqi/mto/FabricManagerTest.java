/*
 *  Copyright 2017 DTCC, Fujitsu Australia Software Technology, IBM - All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.liqi.mto;

import com.liqi.mto.client.FabricClient;
import com.liqi.mto.config.Config;
import com.liqi.mto.manager.FabricManager;
import com.liqi.mto.user.UserContext;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;

public class FabricManagerTest {

    @Test
    public void testCreateChannel() throws Exception {

        FabricManager fabricManager = new FabricManager();
//            Util.cleanUp();
        UserContext orgAdmin = fabricManager.getOrgAdmin(Config.ORG1_USR_ADMIN_PK, Config.ORG1_USR_ADMIN_CERT, Config.ORG1_MSP, Config.ADMIN);

        FabricClient fabClient = new FabricClient(orgAdmin);

        Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
        Peer peer0_org1 = fabClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
        Peer peer1_org1 = fabClient.getInstance().newPeer(Config.ORG1_PEER_1, Config.ORG1_PEER_1_URL);
        Collection<Peer> peers = new ArrayList<>();
        peers.add(peer0_org1);
        peers.add(peer1_org1);
        Channel channel = fabricManager.createChannel(orgAdmin, Config.CHANNEL_NAME, orderer, peers);
        assertNotNull(channel);
    }

    @Test
    public void testJoinChannel() throws Exception {

        FabricManager fabricManager = new FabricManager();
//            Util.cleanUp();
        UserContext orgAdmin = fabricManager.getOrgAdmin(Config.ORG2_USR_ADMIN_PK, Config.ORG2_USR_ADMIN_CERT, Config.ORG2_MSP, Config.ADMIN);

        FabricClient fabClient = new FabricClient(orgAdmin);

        Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
        Peer peer0_org2 = fabClient.getInstance().newPeer(Config.ORG2_PEER_0, Config.ORG2_PEER_0_URL);
        Peer peer1_org2 = fabClient.getInstance().newPeer(Config.ORG2_PEER_1, Config.ORG2_PEER_1_URL);
        Collection<Peer> peers = new ArrayList<>();
        peers.add(peer0_org2);
        peers.add(peer1_org2);
        Channel channel = fabricManager.joinChannel(orgAdmin, Config.CHANNEL_NAME, orderer, peers);
        assertNotNull(channel);

        if(channel != null){
            Collection<Peer> peers1 = channel.getPeers();
            for (Peer peer : peers1){
                System.out.println(peer.getName());
            }
        }
    }

}
