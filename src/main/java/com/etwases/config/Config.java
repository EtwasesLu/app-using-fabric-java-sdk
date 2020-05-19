package com.etwases.config;

import java.io.File;

public class Config {
	
	public static final String ORG1_MSP = "Org1MSP";

	public static final String ORG1 = "org1";

	public static final String ORG2_MSP = "Org2MSP";

	public static final String ORG2 = "org2";

	public static final String ADMIN = "admin";

	public static final String ADMIN_PASSWORD = "adminpw";
	
	public static final String CHANNEL_CONFIG_PATH = "config/contractchannel.tx";
	
	public static final String ORG1_USR_BASE_PATH = "crypto-config" + File.separator + "peerOrganizations" + File.separator
			+ "org1.liqi.com" + File.separator + "users" + File.separator + "Admin@org1.liqi.com"
			+ File.separator + "msp";
	
	public static final String ORG2_USR_BASE_PATH = "crypto-config" + File.separator + "peerOrganizations" + File.separator
			+ "org2.liqi.com" + File.separator + "users" + File.separator + "Admin@org2.liqi.com"
			+ File.separator + "msp";
	
	public static final String ORG1_USR_ADMIN_PK = ORG1_USR_BASE_PATH + File.separator + "keystore";
	public static final String ORG1_USR_ADMIN_CERT = ORG1_USR_BASE_PATH + File.separator + "admincerts";

	public static final String ORG2_USR_ADMIN_PK = ORG2_USR_BASE_PATH + File.separator + "keystore";
	public static final String ORG2_USR_ADMIN_CERT = ORG2_USR_BASE_PATH + File.separator + "admincerts";
	
	public static final String CA_ORG1_URL = "http://192.168.0.208:7054";
	
	public static final String CA_ORG2_URL = "http://192.168.0.208:8054";
	
	public static final String ORDERER_URL = "grpc://192.168.0.208:7050";
	
	public static final String ORDERER_NAME = "orderer.liqi.com";
	
	public static final String CHANNEL_NAME = "contractchannel";
	
	public static final String ORG1_PEER_0 = "peer0.org1.liqi.com";
	
	public static final String ORG1_PEER_0_URL = "grpc://192.168.0.208:7051";
	
	public static final String ORG1_PEER_1 = "peer1.org1.liqi.com";
	
	public static final String ORG1_PEER_1_URL = "grpc://192.168.0.208:7056";
	
    public static final String ORG2_PEER_0 = "peer0.org2.liqi.com";
	
	public static final String ORG2_PEER_0_URL = "grpc://192.168.0.208:8051";
	
	public static final String ORG2_PEER_1 = "peer1.org2.liqi.com";
	
	public static final String ORG2_PEER_1_URL = "grpc://192.168.0.208:8056";
	
	public static final String CHAINCODE_ROOT_DIR = "chaincode";
	
	public static final String CHAINCODE_1_NAME = "contract";
	
	public static final String CHAINCODE_1_PATH = "github.com/chaincode/contract";
	
	public static final String CHAINCODE_1_VERSION = "1.0.0";


}
