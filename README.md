# 重复博弈沙盒 Repeated game sandbox



## 1. 重复博弈
众所周知的囚徒困境中，博弈双方可以选择“合作”或“背叛”。只有当双方足够信任对方时，才有可能相互合作来获得最大的共同利益。然而“自私的基因”会使得玩家有“背叛”对方的倾向：若对方选择“合作”，那么自己可以从中渔利；若对方也选择“背叛”，至少自己不会一无所得。不论对手是选择“合作”还是“背叛”，“背叛”对方总是可以对自己最有利。这体现了在非零和博弈中，个人做出理性选择却往往导致集体的非理性。

囚徒困境似乎再简单不过了，然而，一旦进行多轮简单博弈，策略就变得种类多样并且有趣了起来。从简单博弈衍生出来的最直接的两个策略是“永远合作”和“永远背叛”，我们称这两种玩家为“全球主义者”和“自私者”。前者以所有玩家的共同利益为最高目标，所以即使对方有可能背叛自己，仍总是会选择“合作”。后者坚信“人不为己，天诛地灭”，只关心自己的得分，所以总会打出“背叛”牌。这两个策略不管进行多少轮博弈，结果只有一个：好人吃亏。有没有一个能够既可以与好人合作，又不会被坏人坑太惨的策略呢？心理学家和博弈学家阿纳托尔（Anatol Papoport）教授提出了一个相当高效的策略：以牙还牙（Tit for Tat）。采取该策略的玩家在第一次博弈中选择“合作”，而在以后的博弈中，会选择上一轮对方对待自己的方法。那么，当“以牙还牙”要与“全球主义者”进行10轮比赛时，由于对手总是选择合作，所以双方会持续10轮相互合作，共同拿到很高的分数。而当“以牙还牙”要与“自私者”比赛10次时，虽然第一次被“自私者”背叛，对方获利，自己一无所得，但在后续的比赛中，它会报复对手，因为自私者在上一轮选择了“背叛”自己。这样在后续9轮里会和“自私者”相互伤害而获得一样的较低分数。“以牙还牙”被认为是最可靠的基本策略。




## 2. 编译和执行

重复博弈沙盒以Java编写，可以使用maven编译（已提供pom.xml文件），或由eclipse打开编译运行（已提供.project文件）。

## 3. 沙盒接口

## 4. 示例说明

