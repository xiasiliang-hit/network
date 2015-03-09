#$local_run_env='./network'

cd ./network_local/PeerToPeer1
java -jar netproject2.jar 1001 
cd ..

cd ./network_local/PeerToPeer2
java -jar netproject2.jar 1002
cd ..

cd ./network_local/PeerToPeer3
java -jar netproject2.jar 1003
cd ..

cd ./network_local/PeerToPeer4
java -jar netproject2.jar 1004
cd ..

cd ./network_local/PeerToPeer5
java -jar netproject2.jar 1005
cd ..

cd ./network_local/PeerToPeer6
java -jar netproject2.jar 1006
cd ..
