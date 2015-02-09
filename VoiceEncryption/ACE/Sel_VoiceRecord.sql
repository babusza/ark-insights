USE [VoiceRecord3]
GO

/****** Object:  StoredProcedure [dbo].[Sel_VoiceRecord]    Script Date: 01/05/2015 15:03:52 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO



CREATE PROCEDURE [dbo].[Sel_VoiceRecord] 

@StartDate datetime,
@EndDate datetime,
@ServiceID int, -- can all (0)
@Agent varchar(200), -- can all ""
@PhoneNo varchar(200), -- no data ""
@CustomerName varchar(200), -- no data ""
@Called int ,-- 0=don't use 1=use
@Mobile int, -- 0=don't use 1=use
@Home int, -- 0=don't use 1=use
@Office int, -- 0=don't use 1=use
@CallRefID varchar(200), -- no data ""
@UserID varchar(30),
@TrackID int, -- no data (0)
@CustomerID varchar(200), -- no data ""
@Disposition int, -- can all (0)
@RecordingSec int, -- no data (0)
@SecFact varchar(1), -- M=More than L=Less than
@WorkGroup  int, -- 0=All 
@TrackShow int,
@FlageBat int, 
@SearchFlage int

AS
BEGIN
	SET NOCOUNT ON;
	
	DECLARE 
	@FilterService varchar(200),
	@FilterAgent varchar(200),
	@FilterPhone varchar(120),
	@FilterCustomer varchar(200),
	@FilterRefID varchar(200),
	@FilterTrackID varchar(200),
	@FilterCustID varchar(200),
	@FilterDisposition varchar(200),
	@FilterRecSec varchar(200),
	@FilterLastTrack varchar(80),
	@WorkgroupName varchar(100),
	@WorkgroupID varchar(100),
	@FirstTrack int = 0 ,
	@LastTrack int = 0,
	@MaxPage int = 500,	
	@StateSQL varchar(max)
	
	IF(@SearchFlage = 1)
		SET @StateSQL = 'SELECT COUNT(TrackID) AS TOTAL FROM VoiceRecord WHERE'
	Else
		SET @StateSQL = 'SELECT TOP 500 * FROM VoiceRecord WHERE'
			
	SET @StateSQL = @StateSQL + ' CalledDate between ''' + convert(varchar(20),@StartDate,120) + ''' and ''' + convert(varchar(20),@EndDate,120) + '''' + ' and DispositionID >= 64'
	
	Select @WorkgroupID=WorkGroupID FROM SupervisorByGroupID WHERE USER_ID = @UserID -- select workgroup 
	
	Select @WorkgroupName=WorkGroupName FROM UserAspect WHERE UserID = @UserID -- select workgroup 

	IF (@ServiceID = 0)
		SET @FilterService = ''
	ELSE
	  SET @FilterService = ' and ServiceID = ' + convert(varchar(15),@ServiceID)
	  --  SET @FilterService = ' and ServiceID = ' + @ServiceID
	
	IF (@Agent = '')
	Begin
		IF (@WorkgroupID is null)
		Begin
			IF (@WorkgroupName != 'QC')
				SET @FilterAgent = ' AND UserID = ''' + @UserID + ''''
			ELSE
			Begin
			IF (@WorkGroup = 0)
				SET @FilterAgent = ''
			ELSE
				SET @FilterAgent = ' and UserID in (Select UserID From UserAspect where WorkGroupID in ( ' +  convert(varchar(15),@WorkGroup) + ' ))'
			End
		End
		Else
		Begin
			IF (@WorkGroup = 0)
				SET @FilterAgent = ' and UserID in (Select UserID From UserAspect where WorkGroupID in ( ' + @WorkgroupID + ' ))'
			ELSE
				SET @FilterAgent = ' and UserID in (Select UserID From UserAspect where WorkGroupID in ( ' + convert(varchar(15),@WorkGroup) + ' ))'
		End
	END
	ELSE
		SET @FilterAgent = ' and UserID = ''' + @Agent + ''''
		
	IF (@PhoneNo = '')
		SET @FilterPhone = ''
	ELSE
	BEGIN
	
		IF (LEN(@PhoneNo) > 11)
		SET @FilterPhone = ' and PhoneNo = ''' + @PhoneNo + ''''
		ELSE
		Begin
		
		SET @FilterPhone = ' and ''' + @PhoneNo + ''' in ( PARAM7'
		
		IF (@Called = 1)
			SET @FilterPhone = @FilterPhone + ' ,PhoneNo'
		
		IF (@Mobile = 1)
			SET @FilterPhone = @FilterPhone + ' ,Param4'
			
		IF (@Home = 1)
			SET @FilterPhone = @FilterPhone + ' ,Param5'
			
		IF (@Office = 1)
			SET @FilterPhone = @FilterPhone + ' ,Param6'
		
		SET @FilterPhone = @FilterPhone + ' )'
		End
	END
	
	IF (@CustomerName = '')
		SET @FilterCustomer = ''
	ELSE
		SET @FilterCustomer = ' and Param2 = ''' + @CustomerName + ''''
		
	IF (@CallRefID = '')
		SET @FilterRefID = ''
	ELSE
		SET @FilterRefID = ' and Param20 = ''' + @CallRefID + ''''
		
	IF (@TrackID = 0)
		SET @FilterTrackID = ''
	ELSE
		SET @FilterTrackID = ' and TrackID = ' + convert(varchar(15),@TrackID)
		
	IF (@CustomerID = '')
		SET @FilterCustID = ''
	ELSE
		SET @FilterCustID = ' and Param1 = ''' + @CustomerID + ''''
	
	IF (@Disposition = '')
		SET @FilterDisposition = ''
	ELSE
		SET @FilterDisposition = ' and DispositionID = ' + convert(varchar(15),@Disposition)

	BEGIN	    
		IF (@SecFact = 'M')
			SET @FilterRecSec = ' and RecordingSecs >= ' + convert(varchar(15),@RecordingSec)
		ELSE
			SET @FilterRecSec = ' and RecordingSecs < ' + convert(varchar(15),@RecordingSec)
	END	
	
	IF(@SearchFlage = 1)
		SET @FilterLastTrack = ''
	Else
	Begin
		IF(@FlageBat >= 0)
			SET @FilterLastTrack = ' and TrackID > ' + convert(varchar(15),@TrackShow) + ' ORDER BY TrackID'							
		ELSE
			SET @FilterLastTrack = ' and TrackID < ' + convert(varchar(15),@TrackShow) + ' ORDER BY TrackID DESC'						
	End

		
	
		--+ ' and < ' +convert(varchar(15),@LastTrack)
		--	SET @FilterRecSec = ' and TrackID between ' + convert(varchar(15),@FirstTrack)+ ' and ' +convert(varchar(15),@LastTrack)
	
	
	SET @StateSQL = @StateSQL + @FilterService + @FilterAgent + @FilterPhone + @FilterCustomer + @FilterRefID + @FilterTrackID + @FilterCustID + @FilterDisposition + @FilterRecSec + @FilterLastTrack
	
	
	
	EXEC (@StateSQL)
--	select (@StateSQL)
	
	--select @StateSQL
END   

GO


