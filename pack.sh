target='./PeerToPeer'
from='./netproject2' 

cp -Rf $from/src $target
cp $from/dist/netproject2.jar $target
cp $from/Common.cfg $target
cp $from/PeerInfo.cfg $target
#copy data file for 1001 node
cp -Rf $from/peer_1001 $target

#copy to local running env
local_run_env='./network_local'
#$peer='PeerToPeer'
rm -Rf $local_run_env
mkdir $local_run_env

cp -Rf $target $local_run_env
mv $local_run_env/PeerToPeer $local_run_env/PeerToPeer1

cp -Rf $target $local_run_env
mv $local_run_env/PeerToPeer $local_run_env/PeerToPeer2

cp -Rf $target $local_run_env
mv $local_run_env/PeerToPeer $local_run_env/PeerToPeer3

cp -Rf $target $local_run_env
mv $local_run_env/PeerToPeer $local_run_env/PeerToPeer4

cp -Rf $target $local_run_env
mv $local_run_env/PeerToPeer $local_run_env/PeerToPeer5

cp -Rf $target $local_run_env
mv $local_run_env/PeerToPeer $local_run_env/PeerToPeer6
