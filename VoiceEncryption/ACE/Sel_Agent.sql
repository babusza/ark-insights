USE [VoiceRecord3]
GO

/****** Object:  StoredProcedure [dbo].[Sel_Agent]    Script Date: 01/05/2015 14:59:59 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO



CREATE PROCEDURE [dbo].[Sel_Agent] 
@WorkGroup int
AS
BEGIN
	select * from dbo.UserAspect where WorkGroupID=@WorkGroup
END
GO


