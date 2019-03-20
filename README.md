# God
God is a high performance TCP/UDP reverse proxy server with load balancing capabilities.

God is mainly build to act as a load balancer and to route traffic to the desired backend server with the capability to select between multiple balancing algorithms. Through the simplicity of the config file you can set it up really quick and it just works.

A cool feature is that you can add or remove backend server through a very simple RESTful API and that these servers are automatically removed or added live to the load balancer
and are directly accessible and usable.
The RESTful API also gives you the opportunity to get live traffic and general stats of God.

# Overview

- [Features](https://github.com/marvinerkes/God#features)
- [Installation](https://github.com/marvinerkes/God#installation)
- [Config](https://github.com/marvinerkes/God#config)
- [UDP](https://github.com/marvinerkes/God#udp)
- [Real-World stats](https://github.com/marvinerkes/God#real-world-stats)
- [RESTful API](https://github.com/marvinerkes/God#restful-api)

# Features

- built on top of [netty](https://github.com/netty/netty)
- load balancing
- multiple strategies (round robin, random, least connections, fastest)
- dynamic server adding/removing/listing (simple RESTful API)
- health check (ping probe, interval configurable in ms)
- offline/online server management (removing and adding back to the LB)
- configurable boss threads (1 should be fine for most normal use cases)
- configurable worker threads (recommended value is cpu cores * 2)
- logging (debug logging configurable)
- live traffic and other stats
- persistent total read/written stats
- TCP and UDP support

# Installation

First of all make sure you have Java 8 installed.

Download the latest God version from the [release page](https://github.com/marvinerkes/God/releases) and start it like this:

```
java -jar god-1.7.2.jar
```

Stop it by typing ```end``` followed by an enter press. Configure the config.iris file in the same directory to fit your needs and restart God.

# Config

Very simple but neat config format based on my project [Iris](https://github.com./marvinerkes/Iris/).

Available balance strategies are:

| Strategy  | Description |
| --------- | ----------- |
| ROUND_ROBIN | Typical round robin algorithm |
| RANDOM | Random based selection |
| LEAST_CON | The one with the least amount of connections |
| FASTEST | The one with the fastest connection time |

```ini
# The first timeout value is read timeout
# and the second one is write timeout
# Both are in seconds
#
# The boss value is the amount of threads to accept connections
# The worker value is the amount of threads to handle events
#
# If stats is true God will collect traffic stats that are
# accessible through the RESTful API
general:
    mode tcp
    debug true
    server 0.0.0.0 80
    backlog 100
    boss 1
    worker 4
    timeout 30 30
    balance ROUND_ROBIN
    probe 5000
    stats true

# How the RESTful API should be accessible
# It is recommended to not bind this to 0.0.0.0
# or to a specific external interface
rest:
    server localhost 6000

# Here are all your backend servers
backend:
    api-01 172.16.0.10 8080
    api-02 172.16.0.11 8080
    api-03 172.16.0.12 8080
    api-04 172.16.0.13 8080
```

# UDP

Some special hints for the UDP protocol:

When using the 'mode udp', keep in mind that the connections per second are more likely something like packets or messages per second because there are no connections at any time.
The connections value from the RESTful API will be zero, because no connections exists as mentioned above.

Also important is that the health check will fail if your UDP backend servers doesn't send the "ICMP: Destination/Port Unreachable" packet properly, due to a firewall or something like that.

# Real-World stats

God was tested with over 200 connections (players) and a throughput of 150 Mbit/s.
During the live load God only had 40-60% CPU usage with 1 boss thread and 4 worker threads on a 4 core 8 threads Xeon E3 CPU and a maximum RAM usage of 2 GB.
So God has only used 7.5% of the total CPU power which shows that it can be used nicely in a dynamic cloud infrastructure with small virtual machines.
You can definitely use multiple God instances through DNS-RB to fit your bandwidth and availability needs.

All balancing strategies were tested and all performed perfectly. The health check also works as it should, so it removes dead backend servers and adds these back when they are up again.

As you can see God is a high performance software load balancer with the focus in performance and efficiency.
It has many features to be as flexible and dynamically as possible but also to be simple.

Hardware details tested with:

CPU model name: Intel(R) Xeon(R) CPU E3-1231 v3 @ 3.40GHz  
OS name: Debian GNU/Linux 8 (jessie)  
NIC/Uplink speed: 1 Gbit/s  
RAM: 4 GB

# RESTful API

The API consists of four simple GET paths. Two are with path variables to keep the adding and removing of backend server as simple as possible.

| Path | Example | Description |
| --------- | ----------- | ----------- |
| /god/add/{name}/{ip}/{port} | /god/add/web-01/172.16.0.50/80 | Adds the given backend server to the load balancer |
| /god/remove/{name} | /god/remove/web-01 | Removes the given backend server from the load balancer |
| /god/list | /god/list | Lists the current backend servers which are in the load balancer |
| /god/stats | /god/stats | Live traffic stats from God |

_Responses:_

| Path | Success | Error |
| --------- | ----------- | ----------- |
| /god/add/{name}/{ip}/{port} | ```{"status":"OK","message":"Successfully added server"}``` | ```{"status":"SERVER_ALREADY_ADDED","message":"Server was already added"}``` |
| /god/remove/{name} | ```{"status":"OK","message":"Successfully removed server"}``` | ```{"status":"SERVER_NOT_FOUND","message":"Server not found"}``` |
| /god/list | ```{"backendInfo":[{"name":"api-01","host":"172.16.0.10","port":8080,"connectTime":125.0}],"status":"OK","message":"List received"}``` | ```{"status":"ERROR","message":"Unable to get the balancing strategy"}``` |
| /god/stats | ```{"connections":360,"connectionsPerSecond":10,"onlineBackendServers":3,"currentReadBytes":500,"currentWrittenBytes":25356,"lastReadThroughput":36,"lastWriteThroughput":39864,"totalReadBytes":929,"totalWrittenBytes":705887}``` | ```{"connections":-1,"onlineBackendServers":-1,"currentReadBytes":-1,"currentWriteBytes":-1,"lastReadThroughput":-1,"lastWriteThroughput":-1,"totalReadBytes":-1,"totalWrittenBytes":-1,"status":"ERROR","message":"Stats are disabled"}``` |

### License

Licensed under the GNU General Public License, Version 3.0.
