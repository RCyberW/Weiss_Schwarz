#!/usr/bin/python
import os
import re
import sqlite3
import shutil


conn = sqlite3.connect('CardData.sqlite')
conn.text_factory = str
c = conn.cursor()
items = c.fetchall()
for f in os.listdir('.'):
    if os.path.splitext(f)[1] != '.jpg':
        continue
    imagename = os.path.splitext(f)[0]
    rarity = imagename.split('-')[2]
    print rarity
    rarity = re.sub('\w?\d+[a-z]*','',re.search('\w?\d+.*',rarity).group(0))
    ontid = imagename.replace(rarity,'')#.replace('-','/',1)
    rarity = rarity.replace("RH'",'R_sign')
    rarity = rarity.replace('CH','C_holo')
    rarity = rarity.replace('RH','R_holo')
#    print ontid, rarity
    if rarity == 'SR': 
        ontid += 'S'
    elif rarity == 'RRR':
        ontid += 'R'
    elif rarity == 'SP':
        ontid += 'SP'
    expImageName = ontid + '_' + rarity
    print expImageName
    
    c.execute('select setid,cardid,imagefilename from cardtable where imagefilename LIKE ?',('%'+expImageName+'%',))
    matches = c.fetchall()
    for (setid,cid,imfile) in matches:
        if expImageName in imfile:
            code = re.sub('[^/]*/','',setid)
            print (code,setid,imfile)
            if not os.path.exists(code):
                os.makedirs(code)
            print 'move %s to %s' % (f, code+'/'+imfile)
            shutil.move(f,code+'/'+imfile)




#c.execute('insert into cardtable values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)',(keyid,name,name_e,cardid,ctype,level,color,cost,trigger,power,soul,trait1,trait1_e,trait2,trait2_e,rule,rule_e,flavor,flavor_e))

conn.close()
