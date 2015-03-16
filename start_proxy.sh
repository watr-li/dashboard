#!/bin/bash

function ctrl_c {
    echo "Stopping"
    nginx -p nginx -c nginx.conf -s stop
}

trap ctrl_c INT

echo "Starting"
nginx -p nginx -c nginx.conf &&
    tail -f logs/nginx.log


