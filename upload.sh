#!/bin/sh
dest=erdbeerkuchen.local
scp target/vierzwo $dest:~/
scp src/dist/vierzwo.desktop $dest:~/Desktop/
scp src/dist/wakeup.sh $dest:~/
