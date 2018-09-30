from itertools import permutations
import sys
# 65 256
# 19 378
# 91 230
# 95 361
# 99 400
# 68 367
# 24 367
# 15 323
# 12 322
# 63 432
#0.2 0.2 #5
testcase  = [[96,-178],[92,79],[96,-163],[82,-108],[37,-56]]
#0.40.4 #10
testcase2 = [[65,256],[19,378],[91,230],[95,361],[99,400],[68,367],[24,367],[15,323],[12,322],[63,432]]

used_testcase = testcase2
minimumTardiness = sys.maxsize
minimumCombination = None
for combination in permutations(used_testcase, len(used_testcase)):

    candidateTardiness = 0;
    timestamp = 0
    for job in combination:
        candidateTardiness = candidateTardiness+max(0, timestamp + job[0]-job[1])
        timestamp+=job[0]

    print(combination)
    print(candidateTardiness)
    if candidateTardiness<minimumTardiness:
        minimumTardiness=candidateTardiness
        minimumCombination = combination

print(minimumTardiness)
print(minimumCombination)
