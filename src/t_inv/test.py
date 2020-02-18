import re

# Abro el archivo de Log.
file = open("log.txt")
stringCompleto = file.read()
file.close()

# Reemplazo los saltos de línea porque son problemáticos para las regEx.
stringCompleto = stringCompleto.replace('\n', ',')
stringCompleto = stringCompleto.replace('T0,', '')
stringCompleto = stringCompleto.replace('T17,', '')

print(stringCompleto)

regex = r"""
(?:  #regex entera
    (?:  #primer grupo
    (?: (T1,) (?: (?:T[^\D4](?:\d)?,)*? (?P<T4>T4,))? ) 
    |
    (?: (T2,) (?: (?:T[^\D5](?:\d)?,)*? (?P<T5>T5,))? ) 
    |
    (?: (T3,) (?: (?:T[^\D6](?:\d)?,)*? (?P<T6>T6,))? )   
    ) #primer grupo

    (?:#segundo grupo
    (?(T4)#si encontró T4
    (?: #busca T7-T9, con la distinción que la fruta que encuentres antes de T7 no sea un 9!
    (?:(?:T(?:[^\D9^\D7])(?:\d)?,)*?(T7,) (?: (?:T(?:[^\D8])(?:\d)?,)*? (?P<T4T8>T8,))?)?

 (?(T4T8) | 
    (?: 
(?:T(?:[^\D7^\D9])(?:\d)?,)*?(T9,)     (?:#Si encuentra T9, que busque T10
    (?:  T\d(?:[^\D0])?,)*? (T10,)   (?: #Si encuentra T10, que busque T11
    (?:  T\d(?:[^\D1])?,)*?(T11,)   (?:#Si encuentra T11, que busque T12
    (?:  T\d(?:[^\D2])?,)*?(?P<T4T12>T12,)   )? 
    )?#encuentro de T11
    )?#encuentro de T10
    )?   #encuentro de T9
) #if 2da rama	
) 

    |
    (?(T5)# IF T5 // si encontró T5
    (?:(?: #busca T7-T9, con la distinción que la fruta que encuentres antes de T7 no sea un 9!
    (?:T(?:[^\D9^\D7])(?:\d)?,)*?(T7,) (?: (?:T(?:[^\D8])(?:\d)?,)*? (?P<T5T8>T8,))? )? 
(?(T5T8) |	
(?:
    (?:T(?:[^\D7^\D9])(?:\d)?,)*?(T9,) (?:#Si encuentra T9, que busque T10
    (?:T\d(?:[^\D0])?,)*? (T10,)(?: #Si encuentra T10, que busque T11
    (?:T\d(?:[^\D1])?,)*?(T11,) (?:#Si encuentra T11, que busque T12
    (?:T\d(?:[^\D2])?,)*?(?P<T5T12>T12,)
    )?#encuentro de T11
    )?#encuentro de T10
    )? #encuentro de T9
) # if 2da rama
)? 
    )
    |
    (?(T6)# IF T6 // si encontró T6
    (?:
(?: #busca T7-T9, con la distinción que la fruta que encuentres antes de T7 no sea un 9!
    (?:T(?:[^\D9^\D7])(?:\d)?,)*?(T7,) (?: (?:T(?:[^\D8])(?:\d)?,)*? (?P<T6T8>T8,))?)?
(?(T6T8) |	
    (?:(?:T(?:[^\D7^\D9])(?:\d)?,)*?(T9,) (?:#Si encuentra T9, que busque T10
    (?:T\d(?:[^\D0])?,)*?(T10,)(?: #Si encuentra T10, que busque T11
    (?:T\d(?:[^\D1])?,)*?(T11,) (?:#Si encuentra T11, que busque T12
    (?:T\d(?:[^\D2])?,)*?(?P<T6T12>T12,)
    )?#encuentro de T11
    )?#encuentro de T10
    )? #encuentro de T9
) # if 2da rama
)?
    )|
    )#fin IF T6
    )#fin IF T5
    )#fin IF T4
    ) #segundo grupo
    (?:#tercer grupo

    (?(T4T8) #T4-T8
    #1 y 2
    (?:(?:
    (?:T(?:\d)(?:[^\D3^\D4])?,)*?(T13,) (?: (?:T\d(?:[^\D5])?,)*? (?P<T4T7T15>T15,))? )?

    (?: (?(T4T7T15) | (?:T(?:\d)(?:[^\D3^\D4])?,)*?(T14,) (?: (?:T\d(?:[^\D6])?,)*? (T16,))? ) )? 
    )
    |
    (?(T4T12) #T4-T12
    #3y4
    (?:(?:
    (?:T(?:\d)(?:[^\D3^\D4] )?,)*?(T13,) (?: (?:T\d(?:[^\D5])?,)*? (?P<T4T12T15>T15,))? )?

    (?: (?: (?(T4T12T15) | (?:T(?:\d)(?:[^\D3^\D4])?,)*?(T14,) (?: (?:T\d(?:[^\D6])?,)*? (T16,))? ) 
    ))?)
    |
    (?(T5T8) #T5-T8
    #5y6
    (?:(?:
    (?:T(?:\d)(?:[^\D3^\D4])?,)*?(T13,) (?: (?:T\d(?:[^\D5])?,)*? (?P<T5T8T15>T15,))? )?

    (?: (?(T5T8T15) | (?:T(?:\d)(?:[^\D3^\D4])?,)*?(T14,) (?: (?:T\d(?:[^\D6])?,)*? (T16,))? ) )?
    )
    |
    (?(T5T12) #T5-T12
    #7y8
    (?:(?:
    (?:T(?:\d)(?:[^\D3^\D4])?,)*?(T13,) (?: (?:T\d(?:[^\D5])?,)*? (?P<T5T12T15>T15,))? )?

    (?: (?(T5T12T15) | (?:T(?:\d)(?:[^\D3^\D4])?,)*?(T14,) (?: (?:T\d(?:[^\D6])?,)*? (T16,))? ) )? 
    )
    |
    (?(T6T8) #T6-T8
    #9y10
    (?:
    (?:
    (?:T(?:\d)(?:[^\D3^\D4])?,)*?(T13,) (?: (?:T\d(?:[^\D5])?,)*? (?P<T6T8T15>T15,))? )? 

    (?: (?(T6T8T15) | (?:T(?:\d)(?:[^\D3^\D4])?,)*?(T14,) (?: (?:T\d(?:[^\D6])?,)*? (T16,))? ) )? 
    )
    |
    (?(T6T12) #T6-T12
    #11y12
    (?:(?:
    (?:T(?:\d)(?:[^\D3^\D4])?,)*?(T13,) (?: (?:T\d(?:[^\D5])?,)*? (?P<T6T12T15>T15,))? )? 

    (?: (?(T6T12T15) | (?:T(?:\d)(?:[^\D3^\D4])?,)*?(T14,) (?: (?:T\d(?:[^\D6])?,)*? (T16,))? ) )? 
    )|
    )# fin T6-T12
    )# fin T6-T8
    )# fin T5-T12
    )# fin T5-T8
    )# fin T4-T12
    )# fin T4-T8
    ) #tercer grupo
    ) #regex entera
"""

while True:
    matches = re.search(regex, stringCompleto, re.MULTILINE | re.VERBOSE)

    if matches:
        print("Match was found at {start}-{end}: {match}".format(start=matches.start(), end=matches.end(),
                                                                 match=matches.group()))

        for groupNum in range(0, len(matches.groups())):
            groupNum = groupNum + 1
            if matches.group(groupNum) is not None:
                print(
                    "Group {groupNum} found at {start}-{end}: {group}".format(groupNum=groupNum,
                                                                              start=matches.start(groupNum),
                                                                              end=matches.end(groupNum),
                                                                              group=matches.group(groupNum)))
        print(stringCompleto)
        for groupNum in range(len(matches.groups()), 0, -1):
            if matches.group(groupNum) is not None:
                stringCompleto = stringCompleto[:matches.start(groupNum)] + stringCompleto[matches.end(groupNum):]
                print(stringCompleto)
    else:
        break
if 'T' not in stringCompleto:
    print('Success')
else:
    print('Error')
