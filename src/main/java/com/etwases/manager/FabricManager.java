package com.etwases.manager;


import com.etwases.client.FabricClient;
import com.etwases.config.Config;
import com.etwases.user.UserContext;
import com.etwases.util.Util;
import org.hyperledger.fabric.sdk.*;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Etwas
 * @version 1.0
 * @date 2020/2/21 11:59
 */
public class FabricManager {

    private static final Logger log = Logger.getLogger(FabricManager.class.getName());

    private static final String connectionProfilePath = System.getProperty("user.dir")
            + File.separator + "src" + File.separator + "main" + File.separator + "resources"
            + File.separator + "network-config.yaml";

    /**
     * 此方法意义不大
     * 创建通道前需要在运行Fabric的服务器上创建配置区块
     * @param orgAdmin
     * @param channelName
     * @param orderer
     * @param peers
     * @return
     */
    public Channel createChannel(UserContext orgAdmin, String channelName, Orderer orderer, Collection<Peer> peers){
        try {
//            Util.cleanUp();

            FabricClient fabClient = new FabricClient(orgAdmin);

            ChannelConfiguration channelConfiguration = new ChannelConfiguration(new File(Config.CHANNEL_CONFIG_PATH));

            byte[] channelConfigurationSignatures = fabClient.getInstance()
                    .getChannelConfigurationSignature(channelConfiguration, orgAdmin);


            Channel mychannel = fabClient.getInstance().newChannel(channelName, orderer, channelConfiguration,
                    channelConfigurationSignatures);

            if(peers == null || peers.size() <= 0){
                Peer peer0_org1 = fabClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
                Peer peer1_org1 = fabClient.getInstance().newPeer(Config.ORG1_PEER_1, Config.ORG1_PEER_1_URL);
                peers.add(peer0_org1);
                peers.add(peer1_org1);
            }

            mychannel.addOrderer(orderer);

            for (Peer peer : peers){
                mychannel.joinPeer(peer);
            }



            mychannel.initialize();

            log.log(Level.INFO, "Channel created "+mychannel.getName());
            Collection tmpPeers = mychannel.getPeers();
            Iterator peerIter = tmpPeers.iterator();
            while (peerIter.hasNext()){
                Peer pr = (Peer) peerIter.next();
                log.log(Level.INFO,pr.getName()+ " at " + pr.getUrl());
            }

            return mychannel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Channel joinChannel(UserContext orgAdmin, String channelName, Orderer orderer, Collection<Peer> peers){
        try{
            FabricClient fabClient = new FabricClient(orgAdmin);
            Channel channel = fabClient.getInstance().newChannel(channelName);
            channel.addOrderer(orderer);

            for(Peer peer : peers){
                boolean isBookChannel = false;
                Set<String> channelNames = fabClient.getInstance().queryChannels(peer);
                if(channelNames != null && channelNames.size() > 0){
                    for(String oldChannelName : channelNames){
                        if(oldChannelName.equals(channelName)){
                            isBookChannel = true;
                            break;
                        }
                    }
                }

                if(!isBookChannel){
                    channel.joinPeer(peer);
                    log.log(Level.INFO, "peer "+ peer.getName() + "加入通道 "+ channel.getName() +"成功！");
                }else {
                    channel.addPeer(peer);
                    log.log(Level.INFO, "peer "+ peer.getName() + "已经加入通道 "+ channel.getName() +"，无需重新加入！");
                }
            }
            channel.initialize();
            return channel;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public boolean deployInstantiateChaincode(){

        return false;
    }

    public Collection<ProposalResponse> invoke(){

        return null;
    }

    public UserContext getOrgAdmin(String orgUserAdminPk, String orgUserAdminCert, String orgMsp, String admin) throws Exception{
        UserContext orgAdmin = new UserContext();
        File pkFolder1 = new File(orgUserAdminPk);
        File[] pkFiles1 = pkFolder1.listFiles();
        File certFolder1 = new File(orgUserAdminCert);
        File[] certFiles1 = certFolder1.listFiles();
        Enrollment enrollOrg1Admin = Util.getEnrollment(orgUserAdminPk, pkFiles1[0].getName(),
                orgUserAdminCert, certFiles1[0].getName());
        orgAdmin.setEnrollment(enrollOrg1Admin);
        orgAdmin.setMspId(orgMsp);
        orgAdmin.setName(admin);

        return orgAdmin;
    }

    /**
     * 重建现有通道
     * @param client
     * @param channelName
     * @param orderers
     * @param peers
     * @return
     * @throws Exception
     */
    private Channel reconstructChannel(HFClient client, String channelName, Collection<Orderer> orderers, Collection<Peer> peers) throws Exception {

        Channel channel = null;

        try {
            channel = client.newChannel(channelName);
            if (orderers != null && orderers.size() > 0){
                for (Orderer orderer : orderers){
                    channel.addOrderer(orderer);
                }
            }
            if(peers != null && peers.size() > 0){
                for(Peer peer : peers){
                    channel.addPeer(peer);
                }
            }

            return channel;
        } catch (Exception e) {
            throw new Exception("重建现有通道异常：" + e);
        }
    }

    public static void main(String[] args) {

    }


}
