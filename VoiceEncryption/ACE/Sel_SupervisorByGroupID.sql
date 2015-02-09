USE [VoiceRecord3]
GO

/****** Object:  StoredProcedure [dbo].[Sel_SupervisorByGroupID]    Script Date: 01/05/2015 15:02:01 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbo].[Sel_SupervisorByGroupID] 
@UserID varchar(250)
	AS
	
Begin
	Declare @F_Workgroup varchar(80),
	@F_WorkgroupName varchar(80)
	
	select @F_Workgroup=WorkGroupID from SupervisorByGroupID where User_Id=@UserID
	select @F_WorkgroupName=WorkGroupName from UserAspect where UserId=@UserID
	IF(@F_Workgroup is not null)
	BEGIN
		exec ('select WorkGroupID, min(WorkGroupName) as WorkGroupName from UserAspect where WorkGroupID in (' + @F_Workgroup + ') group by WorkGroupID')
	END
	ELSE
	BEGIN
		--IF (@F_WorkgroupName = 'QC')
		--	select WorkGroupID, min(WorkGroupName) as WorkGroupName from UserAspect group by WorkGroupID
		--ELSE
			select WorkGroupID, min(WorkGroupName) as WorkGroupName from UserAspect where UserID = @UserID  group by WorkGroupID
	END	
End
GO


