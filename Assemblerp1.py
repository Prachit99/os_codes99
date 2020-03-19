two_byte = ["AR","BALR","BR","SR"]
fourbyte = ["L","A","S","ST","COM","LA","BNE"]

pseudoOP = ["USING","EQU","DS","DC","STRAT","END","LTORG"]
SymTab = []
Symbols = []
LitTab = []
BaseTab = []
LC = 0
countS = 1
countL = 1
def pseudo(word):
    if word in pseudoOP:
        return True
    return False


def isLiteral(word):
    if ",=F'" in word:
        return True
    return False

file = open('input.txt','r')
Lines = file.readlines()
for line in Lines:
    line.strip()
    arg = line.split()
    if len(arg) >= 3:
        if arg[1] == "EQU":
            label = [arg[0],int(arg[2]),'A',1,countS]
            countS += 1
        elif arg[1] == "START":
          label = [arg[0],LC,'R',1,countS]
          countS += 1 
        else:
            label = [arg[0],LC,'R',4,countS]
            countS += 1
        SymTab.append(label)
        Symbols.append(arg[0])
        n = 1
    elif len(arg) <= 2:
        n = 0

    if pseudo(arg[n]):
        if arg[n] == "END" and len(LitTab[0]) == 2:
            if LC%4 != 0:
                while LC%4 != 0:
                    LC += 1
            for lit in LitTab:
                lit.append(LC)
                LC += 4
        elif arg[n] == "DC" or arg[n] == "DS":
            if LC%4 == 0:
                LC += 4
            else:
                while LC%4 != 0:
                    LC += 1
                LC += 4
        elif arg[n] == "USING":
            sub = arg[n+1].split(',')
            if sub[0] == '*':
                label = [sub[1],LC]
            else:
                label = [sub[1],sub[0]]
            BaseTab.append(label)
        elif arg[n] == "LTORG":
            if LC%8 != 0:
                while LC%8 != 0:
                    LC += 1
            for lit in LitTab:
                lit.append(LC)
                LC += 4
            
    else:
        if arg[n] in two_byte:
            LC += 2
        elif arg[n] in fourbyte:
            LC += 4
    
    if len(arg) != 1: 
        if isLiteral(arg[n+1]):
            x = arg[n+1]
            #print(f'x = {x}')
            for i in range(len(x)):
                if x[i] == ',':
                    lit = arg[n+1][i+1:]
                    Symbols.append(lit)
                    break
            label = [lit,'R',4,countL]
            countL += 1
            LitTab.append(label)
    #print(line)
print("-------Pass 1--------")
print("Symbol table: ")
print("ID\tSymbol\tValue\tR/A\tLength")
for i in SymTab:
  print(f'{i[4]}\t{i[0]}\t{i[1]}\t{i[2]}\t{i[3]}')

print()
print("Literal table: ")
print("ID\tLiteral\tR/A\tLength\tValue")
for i in LitTab:
  print(f'{i[3]}\t{i[0]}\t{i[1]}\t{i[2]}\t{i[4]}')

print()
print("Base table: ")
print("Register\tContent")
for i in BaseTab:
  print(f'{i[0]}\t\t{i[1]}')


print()
print("-----Intermediate code-------")
#inter = []
LC = 0

def getID(temp):
    for i in range(len(temp)):
        t = temp[i]
        if t in Symbols:
            for sym in SymTab:
                if sym[0] == t:
                    temp[i] = "ID#" + str(sym[4])
                    break
            for lit in LitTab:
                if lit[0] == t:
                    temp[i] = "LT#" + str(lit[3])
    return temp



for line in Lines:
    arg = line.split()
    if len(arg) >= 3:
        n = 1
    elif len(arg) <= 2:
        n = 0
    
    if pseudo(arg[n]):
        if arg[n] == "DC":
            if LC%4 != 0:
                while LC%4 != 0:
                    LC += 1
            print(f'{LC}\t{arg[n]}\t{arg[n+1]}')
            LC += 4
        elif arg[n] == "DS":
            if LC%4 != 0:
                while LC%4 != 0:
                    LC += 1
            print(f'{LC}\t{arg[n]}\t{arg[n+1]}')
            LC += int(arg[n+1].replace('F',''))*4
        elif arg[n] == "END":
            print(f'{LC}\t{arg[n]}')
        elif arg[n] == "LTORG":
            print(f'{LC}\t--\t--')
            if LC%8 != 0:
                while LC%8 != 0:
                    LC += 1
            LC += (countL-1)*4
    else:
        if arg[n] in two_byte:
            temp = arg[n+1].split(',')
            temp = getID(temp)
            if arg[n] == "BR":
                arg[n+1] = temp[0]
            else: 
                arg[n+1] = temp[0]+","+temp[1]
            print(f'{LC}\t{arg[n]}\t{arg[n+1]}')
            LC += 2
        elif arg[n] in fourbyte:
            temp = arg[n+1].split(',')
            temp = getID(temp)  
            if arg[n] == "BNE":
                arg[n+1] = temp[0]
            else:
                arg[n+1] = temp[0]+","+temp[1]
            print(f'{LC}\t{arg[n]}\t{arg[n+1]}')
            LC += 4
              
    



print()
print("-------Pass 2-------")
LC = 0
final = []


def getValue(temp):
    for i in range(len(temp)):
        t = temp[i]
        if t in Symbols:
            for sym in SymTab:
                if sym[0] == t:
                    temp[i] = str(sym[1])
                    break
            for lit in LitTab:
                if lit[0] == t:
                    temp[i] = str(lit[4] - BaseTab[0][1])
    return temp


def digit(num):
    if num == '0' or num == '1' or num == '2' or num == '3' or num == '4' or num == '5' or num == '6' or num == '7' or num == '8' or num == '9':
        return True
    return False

for line in Lines:
    arg = line.split()
    if len(arg) >= 3:
        n = 1
    elif len(arg) <= 2:
        n = 0

    if pseudo(arg[n]):
        if arg[n] == "DC":
            if LC%4 == 0:
                LC += 4
            else:
                while LC%4 != 0:
                    LC += 1
                LC += 4
            arg[n+1] = arg[n+1].replace('F','')
            arg[n+1] = hex(int(arg[n+1].replace('\'','')))
            label = [LC,arg[n+1],"--"]
            final.append(label)
        elif arg[n] == "DS":
            if LC%4 != 0:
                while LC%4 != 0:
                    LC += 1
            var = hex(int(arg[n+1].replace('F','')))
            label = [LC,var,"--"]
            LC += 4*int(arg[n+1].replace('F','')) 
        elif arg[n] == "LTORG":
            label = [LC,"--","--"]
            final.append(label)
            if LC%8 != 0:
                while LC%8 != 0:
                    LC += 1
            for x in LitTab:
                LC += 4
                z = x[0]
                num = ""
                for y in range(len(z)):
                    if digit(z[y]):
                        num += z[y]
                n = hex(int(num)) 
                label = [LC,n,"--"]
                final.append(label)
        elif arg[n] == "END":
            label = [LC,"--","--"]
            final.append(label)
    else:
        if arg[n] in two_byte:
            temp = arg[n+1].split(',')
            temp = getValue(temp)
            if arg[n] == "BR":
                arg[n+1] = temp[0]+",15"
                label = [LC, "BCR", arg[n+1]]
            else:
                arg[n+1] = temp[0]+","+temp[1]
                label = [LC, arg[n], arg[n+1]]
            final.append(label)
            LC += 2
        elif arg[n] in fourbyte:
            temp = arg[n+1].split(',')
            temp = getValue(temp)
            if arg[n] == "BNE":
                arg[n+1] = "7,"+temp[0]+" (0,15)"
                label = [LC, "BC", arg[n+1]]
            else:
                arg[n+1] = temp[0]+","+temp[1]+" (0,15)" 
                label = [LC, arg[n], arg[n+1]]
            
            final.append(label)
            LC += 4
print("Final machine code:")
for i in final:
  print(f'{i[0]}\t{i[1]}\t{i[2]}')
    
 
