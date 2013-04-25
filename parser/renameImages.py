#!/usr/bin/python
import os
import re
import sqlite3
import shutil


conn = sqlite3.connect('CardData.sqlite')
conn.text_factory = str
c = conn.cursor()
c.execute('select setid,la_id,imagefilename from cardtable where 1')
items = c.fetchall()
for (setid,la_id,ifilename) in items:
    imfile = 'card'+str(la_id)+'.jpg'
    if os.path.isfile(imfile):
        code = re.sub('[^/]*/','',setid)
        print (code,setid,la_id,ifilename)
        if not os.path.exists(code):
            os.makedirs(code)
        print 'move %s to %s' % (imfile, code+'/'+ifilename)
        shutil.move(imfile,code+'/'+ifilename)




#c.execute('insert into cardtable values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)',(keyid,name,name_e,cardid,ctype,level,color,cost,trigger,power,soul,trait1,trait1_e,trait2,trait2_e,rule,rule_e,flavor,flavor_e))

conn.close()
