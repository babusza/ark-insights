USE [VoiceRecord3]
GO

/****** Object:  StoredProcedure [dbo].[Sel_UserAspect]    Script Date: 01/05/2015 15:02:31 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO



CREATE PROCEDURE [dbo].[Sel_UserAspect] 
@UserID varchar(50)
AS
	select top 1 * from dbo.UserAspect where UserID=@UserID and UserTypeMask=3


GO


