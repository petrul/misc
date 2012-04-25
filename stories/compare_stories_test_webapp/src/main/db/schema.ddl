--
-- Table structure for table `User`
--

CREATE TABLE IF NOT EXISTS `Event` (
  `id` bigint(20) NOT NULL auto_increment,
  `event_type` varchar(255) NOT NULL,
  `date` datetime default NULL,
  `user` varchar(255) default NULL,
  `response` bigint(20) default NULL,
  `comment` varchar(60000) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK403827AF3D37EEA` (`response`),
  KEY `FK403827A6B16DDDA` (`user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ;

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `InsertionResult` (
  `id` bigint(20) NOT NULL auto_increment,
  `date` datetime NOT NULL,
  `user` varchar(255) default NULL,
  `storyid` int(11) default NULL,
  `optionPresented1` int(11) NOT NULL,
  `optionPresented2` int(11) NOT NULL,
  `chosenOption` int(11) NOT NULL,
  `comment` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FK1431B8C6B16DDDA` (`user`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `login` varchar(255) NOT NULL,
  `firstName` varchar(255) default NULL,
  `lastName` varchar(255) default NULL,
  `sex` char(1) default NULL,
  `email` varchar(255) default NULL,
  `occupation` varchar(255) default NULL,
  `hostIp` varchar(255) default NULL,
  `resolvedHostName` varchar(255) default NULL,
  `comment` varchar(255) default NULL,
  PRIMARY KEY  (`login`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



-- CREATE TABLE IF NOT EXISTS `ComparisonResult` (
--  `id` bigint(20) NOT NULL auto_increment,
--  `comparisonValue` int(11),
--  `date` datetime NOT NULL,
--  `user` varchar(255) default NULL,
--  `leftStoryId` int(11) NOT NULL,
--  `leftStoryVersionId` int(11) NOT NULL,
--  `rightStoryId` int(11) NOT NULL,
--  `rightStoryVersionId` int(11) NOT NULL,
--  PRIMARY KEY  (`id`),
--  KEY `FKE058F9A66B16DDDA` (`user`)
-- ) ENGINE=MyISAM  DEFAULT CHARSET=latin1  ;
