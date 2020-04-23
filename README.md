# secs4j

## Introduction

The secs4j project offers an open source SECS/GEM library written in Java.

The SECS/GEM protocol is the de facto standard in the semiconductor industry used for communication between the host system and semiconductor equipment. 

The secs4j library strives to comply with the following SEMI standards:
* SEMI E05 SECS-II
* SEMI E30 GEM
* SEMI E37 HSMS

## Status

Basic SECS/GEM communication has been implemented, but far more work is required to at least
comply with the minimal set of implementation requirements set by the related SEMI standards.

--- THIS PROJECT IS CURRENTLY INACTIVE ---
理解了SECS-II/GEM的实现逻辑，已完成了通讯的构建以及协议的数据结构开发，开始着手SxFy的消息开发，在进入具体的SVID，CEID和VID开发时，戛然而止，善待解决的问题有：
1.整理各类ID的内容
2.软件与设备间的消息通讯，已建立了MySQL数据，但是没有处理设备主动上报的消息，软件如何接收
3.软件应用的经验缺失，没有投资建立的机会
一些研究分析记录到手稿了，相关资料存档电脑中

## Development

This Java project has a Maven structure.

## Disclaimer

This project is under construction. Use at your own risk!
