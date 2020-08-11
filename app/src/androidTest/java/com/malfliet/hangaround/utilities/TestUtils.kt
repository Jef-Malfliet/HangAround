package com.malfliet.hangaround.utilities

import com.malfliet.hangaround.domain.Activity
import com.malfliet.hangaround.domain.Participant
import com.malfliet.hangaround.domain.Person
import com.malfliet.hangaround.domain.asDatabaseModel

var participant1 = Participant(
    "5e00e8f0520fe30025c17e65",
    "testerABC",
    "5e009fd6d3ffdf073c961d70"
)

var participant2 = Participant(
    "5e00e8f0520fe30025c17e64",
    "testerman",
    "5e009f44d3ffdf073c961d6f"
)

var participant3 = Participant(
    "5e00e8f0520fe30025c17e63",
    "tester",
    "5e00d0185e643d089a71314a"
)

var participantList1 = mutableListOf(participant1, participant2, participant3)

var activity1 = Activity(
    "5e00e8f0520fe30025c17e62",
    "TestActivity",
    "5e00e87f520fe30025c17e61",
    "2020-08-08T22:00:00.000Z",
    "2020-08-09T22:00:00.000Z",
    "Home of Test",
    participantList1,
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit.Quisque in pulvinar leo . Proin consequat tempus sem id egestas.Mauris vel libero in metus dignissim gravida.Curabitur pharetra turpis ornare justo semper dictum.Pellentesque nibh erat, accumsan sed efficitur nec, mollis posuere massa.Ut mi lacus, sagittis ac nibh quis, faucibus dapibus mauris.Morbi commodo bibendum mauris, id suscipit nunc.Nunc sed est in dolor consequat maximus ut non lacus . Duis suscipit posuere justo, ac convallis sapien ullamcorper vitae.Nam vel justo quis erat rutrum bibendum vel eget neque . Mauris sed velit quis diam euismod gravida id sit amet nulla . Morbi dictum elit arcu, eget semper lacus efficitur sed.Mauris pellentesque molestie tristique . Quisque ultricies turpis id erat malesuada, at porta metus porttitor . Integer a dui vel nisl vulputate consequat at at odio.Vestibulum sit amet accumsan ante."
)

var activityDE1 = activity1.asDatabaseModel()

var friendList = mutableListOf(
    "5e009fd6d3ffdf073c961d70",
    "5e00d0185e643d089a71314a"
)
var person1 = Person(
    "5e009f44d3ffdf073c961d6f",
    "123Jef",
    "jef.malfliet@gmail.com",
    friendList
)

var personDE1 = person1.asDatabaseModel()
