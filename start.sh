ssh -n -f -i amazon_danny.pem ubuntu@52.1.104.63 "sh -c 'cd PeerToPeer; nohup java -jar netproject2.jar 1006 > /dev/null 2>&1 &'"
ssh -n -f -i amazon_danny.pem ubuntu@54.86.52.94 "sh -c 'cd PeerToPeer; nohup java -jar netproject2.jar 1001 > /dev/null 2>&1 &'"
