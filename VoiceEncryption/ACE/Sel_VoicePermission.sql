USE [VoiceRecord3]
GO

/****** Object:  StoredProcedure [dbo].[Sel_VoicePermission]    Script Date: 01/05/2015 15:03:10 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbo].[Sel_VoicePermission] 
@UserID varchar(50)
AS
BEGIN
	select * from dbo.VoicePermission where UserId=@UserID
END

GO


