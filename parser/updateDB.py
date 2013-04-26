#!/usr/bin/python
# -*- coding: utf-8 -*-
from __future__ import unicode_literals
import os
import re
import sys
import sqlite3
import codecs
from bs4 import BeautifulSoup

print 'keyid,name,name_e,cardid,type,level,color,cost,trigger,power,soul,trait1,trait1_e,trait2,trait2_e,rule,rule_e,flavor,flavor_e,imagefilename'

conn = sqlite3.connect('CardData.sqlite')
conn.text_factory = str
c = conn.cursor()

files = [e for e in os.listdir('.') if os.path.splitext(e)[1] == '.html']
usedImageNames = []

for f in files:
    name = ''
    name_e = ''
    cardid = ''
    setid = ''
    ctype = ''
    level = ''
    color = ''
    cost = ''
    trigger = ''
    power = ''
    soul = ''
    trait1 = ''
    trait1_e = ''
    trait2 = ''
    trait2_e = ''
    rule = ''
    rule_e = ''
    flavor = ''
    flavor_e = ''
    imagefilename = ''
    fl = open(f,'r')
    #fl = codecs.open(f,'r','utf-8')

    soup = BeautifulSoup(fl.read())
    tabledata = soup.find_all('td')
    name = tabledata[1].contents[0]
    cardid = tabledata[2].contents[0] + ' ' + tabledata[3].contents[0]

    setid = re.match('[^-]*',cardid).group(0)

    ctype = tabledata[6].contents[0]
    if ctype == 'キャラ':
        ctype = 'Character'
    elif ctype == 'イベント':
        ctype = 'Event'
    elif ctype == 'クライマックス':
        ctype = 'Climax'

    color = tabledata[7].contents[0]['src']
    if 'yellow' in color:
        color = 'Yellow'
    elif 'green' in color:
        color = 'Green'
    elif 'red' in color:
        color = 'Red'
    elif 'blue' in color:
        color = 'Blue'

    level = tabledata[8].contents[0]
    if level == '-': level = 'N/A'
    cost= tabledata[9].contents[0]
    if cost == '-': cost = 'N/A'
    power = tabledata[10].contents[0]
    if power == '-': power = 'N/A'
    soul = len(tabledata[11].contents)
    if soul == 0 or tabledata[11].contents[0] == '-': soul = 'N/A'

    triggericons = len(tabledata[12].contents)
    if triggericons <= 1:
        trigger = str(triggericons)
    triggerDict = {'soul':'2','stock':'S','salvage':'D','bounce':'1W','draw':'B','treasure':'G','shot':'1F'}
    if triggericons == 2:
        trigger = tabledata[12].contents[1]['src']
        for key in triggerDict:
            if key in trigger:
                trigger = triggerDict[key]
                break

    traits = tabledata[13].contents[0]
    traits = traits.split(' ・ ')
    if traits[0].strip() != '-':
        trait1 = traits[0].strip()
    if traits[1].strip() != '-':
        trait2 = traits[1].strip()

    ruledata = tabledata[14].contents
    for line in ruledata:
        tp = str(type(line))
        if 'String' in tp:
            rule += unicode(line) + '\n'
    #    if ruledata[idx] != '<br />':
    rule = rule.rstrip()

    flavordata = tabledata[15].contents
    for line in flavordata:
        tp = str(type(line))
        if 'String' in tp:
            flavor += unicode(line) + '\n'
    #    if ruledata[idx] != '<br />':
    flavor = flavor.rstrip()
    if flavor == '-': flavor = ''

    imagefilename = re.sub(' ','_',cardid)
    imagefilename = re.sub('/','-',imagefilename)
    if imagefilename in usedImageNames:
        counter = 1
        if '/SE' in setid or '/WE' in setid:
            imagefiletemp = imagefilename + '_holo'
            while imagefiletemp in usedImageNames:
                imagefiletemp = imagefilename + '_holo' + str(counter)
                counter += 1
            imagefilename = imagefiletemp
        else:
            imagefiletemp = imagefilename + '_alt'
            while imagefiletemp in usedImageNames:
                imagefiletemp = imagefilename + '_alt' + str(counter)
                counter += 1
            imagefilename = imagefiletemp
    usedImageNames.append(imagefilename)
    imagefilename += '.jpg'

    print cardid, imagefilename
        



    #print '{keyid},{name},{name_e},{cardid},{ctype},{level},{color},{cost},{trigger},{power},{soul},{trait1},{trait1_e},{trait2},{trait2_e},{rule},{rule_e},{flavor},{flavor_e}'.format(keyid=keyid,name=name,name_e=name_e,cardid=cardid,ctype=ctype,level=level,color=color,cost=cost,trigger=trigger,power=power,soul=soul,trait1=trait1,trait1_e=trait1_e,trait2=trait2,trait2_e=trait2_e,rule=rule,rule_e=rule_e,flavor=flavor,flavor_e=flavor_e)
    if setid != '':
        c.execute('''
        insert into cardtable
        (name,name_e,setid,cardid,type,level,color,cost,trigger,power,soul,trait1,trait1_e,trait2,trait2_e,rule,rule_e,flavor,flavor_e,imagefilename)
        values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)''',
        (name,name_e,setid,cardid,ctype,level,color,cost,trigger,power,soul,trait1,trait1_e,trait2,trait2_e,rule,rule_e,flavor,flavor_e,imagefilename))

conn.commit()
conn.close()
